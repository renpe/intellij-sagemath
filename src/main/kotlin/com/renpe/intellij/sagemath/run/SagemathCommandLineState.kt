package com.renpe.intellij.sagemath.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ParametersList
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.SystemInfo
import com.renpe.intellij.sagemath.run.wsl.SagemathWslSupport
import com.renpe.intellij.sagemath.settings.SagemathSettings
import java.io.File
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class SagemathCommandLineState(
    private val configuration: SagemathRunConfiguration,
    environment: ExecutionEnvironment,
) : CommandLineState(environment) {

    private data class ResolvedConfig(
        val interpreter: String,
        val pythonMode: Boolean,
        val preparseOnly: Boolean,
        val extraArgs: String,
        val wslDistribution: String,
        val useWsl: Boolean,
        val condaPath: String,
        val condaEnv: String,
    ) {
        /** True when the sage call should be wrapped in `conda run`. */
        val useConda: Boolean get() = condaEnv.isNotBlank()
    }

    override fun startProcess(): ProcessHandler {
        cleanupStaleTempScriptsOnce()
        val resolved = resolve()

        val scriptHostPath = configuration.scriptPath
        if (scriptHostPath.isBlank()) throw ExecutionException("No SageMath script selected.")
        if (!File(scriptHostPath).isFile) throw ExecutionException("SageMath script not found: $scriptHostPath")

        val scriptForSage = if (resolved.useWsl) {
            SagemathWslSupport.toWslPath(resolved.wslDistribution, scriptHostPath)
        } else scriptHostPath

        val workingDirHost = configuration.workingDirectory.ifBlank { File(scriptHostPath).parent ?: "" }
        val workingDirForWsl = if (resolved.useWsl && workingDirHost.isNotBlank()) {
            SagemathWslSupport.toWslPath(resolved.wslDistribution, workingDirHost)
        } else ""

        val sageArgs = buildSageArgs(resolved, scriptForSage)
        // The exe and the args ahead of the script — either just the sage
        // interpreter, or `conda run -n <env> --no-capture-output --live-stream sage`.
        val (exe, leadingArgs) = buildInterpreterCall(resolved)
        val fullArgs = leadingArgs + sageArgs

        val pidFilePath = if (resolved.useWsl) "/tmp/sage-${UUID.randomUUID()}.pid" else ""

        val commandLine = if (resolved.useWsl) {
            buildWslCommandLine(
                exe = exe,
                args = fullArgs,
                workingDir = workingDirForWsl,
                pidFilePath = pidFilePath,
                wslDistribution = resolved.wslDistribution,
            )
        } else {
            buildNativeCommandLine(
                exe = exe,
                args = fullArgs,
                workingDir = if (workingDirHost.isNotBlank()) File(workingDirHost) else null,
            )
        }

        val handler: KillableProcessHandler = if (resolved.useWsl) {
            val process = commandLine.createProcess()
            val displayLine = buildDisplayCommandLine(exe, fullArgs)
            SagemathWslProcessHandler(
                process,
                displayLine,
                Charsets.UTF_8,
                resolved.wslDistribution,
                pidFilePath,
            )
        } else {
            KillableProcessHandler(commandLine)
        }

        ProcessTerminatedListener.attach(handler)
        return handler
    }

    /**
     * Decide what the actual process exe is, and which args go in front
     * of the script. With conda, the exe becomes the conda binary and the
     * sage interpreter slides into the args list after `conda run …`.
     */
    private fun buildInterpreterCall(resolved: ResolvedConfig): Pair<String, List<String>> {
        if (!resolved.useConda) return resolved.interpreter to emptyList()
        val condaExe = resolved.condaPath.ifBlank { "conda" }
        val args = listOf(
            "run",
            "-n", resolved.condaEnv,
            "--no-capture-output",
            "--live-stream",
            resolved.interpreter,
        )
        return condaExe to args
    }

    private fun buildDisplayCommandLine(exe: String, args: List<String>): String =
        (listOf(exe) + args).joinToString(" ", transform = ::quoteForDisplay)

    private fun quoteForDisplay(arg: String): String {
        if (arg.isEmpty()) return "\"\""
        val needsQuoting = arg.any { it.isWhitespace() || it == '"' || it == '\\' }
        if (!needsQuoting) return arg
        val escaped = arg.replace("\\", "\\\\").replace("\"", "\\\"")
        return "\"$escaped\""
    }

    private fun resolve(): ResolvedConfig {
        val s = SagemathSettings.getInstance().state

        val interpreter = configuration.interpreterPath.ifBlank { s.interpreterPath }
            .ifBlank { throw ExecutionException("SageMath interpreter not set. Open Settings → Tools → SageMath.") }
        val wsl = configuration.wslDistribution.ifBlank { s.defaultWslDistribution }
        val useWsl = SystemInfo.isWindows && wsl.isNotBlank()

        val extra = listOf(s.defaultExtraArgs, configuration.extraArgs)
            .filter { it.isNotBlank() }
            .joinToString(" ")

        val condaEnv = configuration.condaEnv.ifBlank { s.defaultCondaEnv }
        val condaPath = configuration.condaPath.ifBlank { s.defaultCondaPath }
            .ifBlank { if (condaEnv.isNotBlank()) deriveCondaFromSage(interpreter) else "" }

        return ResolvedConfig(
            interpreter = interpreter,
            pythonMode = configuration.pythonMode,
            preparseOnly = configuration.preparseOnly,
            extraArgs = extra,
            wslDistribution = wsl,
            useWsl = useWsl,
            condaPath = condaPath,
            condaEnv = condaEnv,
        )
    }

    /**
     * Try to infer the conda binary from the sage interpreter path. Conda
     * installations follow the convention `<root>/envs/<name>/bin/sage`,
     * with `<root>/bin/conda` next to the envs directory. This avoids
     * forcing the user to fill the conda-path field for the standard
     * miniconda / miniforge layout.
     */
    private fun deriveCondaFromSage(sagePath: String): String {
        if (sagePath.isBlank()) return "conda"
        val match = Regex("""^(.*)/envs/[^/]+/bin/sage$""").matchEntire(sagePath)
            ?: return "conda"
        return "${match.groupValues[1]}/bin/conda"
    }

    private fun buildSageArgs(resolved: ResolvedConfig, scriptForSage: String): List<String> {
        val args = mutableListOf<String>()
        when {
            resolved.preparseOnly -> args += "--preparse"
            resolved.pythonMode -> args += "--python"
        }
        if (resolved.extraArgs.isNotBlank()) {
            args += ParametersList.parse(resolved.extraArgs).toList()
        }
        args += scriptForSage
        if (configuration.scriptArgs.isNotBlank()) {
            args += ParametersList.parse(configuration.scriptArgs).toList()
        }
        return args
    }

    private fun buildNativeCommandLine(
        exe: String,
        args: List<String>,
        workingDir: File?,
    ): GeneralCommandLine {
        var cmd = GeneralCommandLine()
            .withExePath(exe)
            .withParameters(args)
            .withCharset(Charsets.UTF_8)
        if (workingDir != null) cmd = cmd.withWorkDirectory(workingDir)
        return cmd
    }

    private fun buildWslCommandLine(
        exe: String,
        args: List<String>,
        workingDir: String,
        pidFilePath: String,
        wslDistribution: String,
    ): GeneralCommandLine {
        val sageCall = (listOf(shellQuote(exe)) + args.map(::shellQuote)).joinToString(" ")

        // See MagmaCommandLineState for the design rationale; same approach
        // here: write the wrapper to a Windows temp file, pass its WSL path
        // to /bin/sh, capture our own PID before exec for kill-on-stop.
        //
        // We deliberately do NOT wrap in `setsid` here. `setsid` without
        // `-w` forks the child and exits the parent, which causes wsl.exe
        // to tear down the stdout/stderr pipe — sage keeps running but
        // its output is dropped. The wsl.exe stdio bridge is fragile and
        // can only see output from the process it directly waits on.
        val script = buildString {
            append("PIDFILE=").append(shellQuote(pidFilePath)).append('\n')
            append("set -C\n")
            append("echo \$\$ > \"\$PIDFILE\" || exit 1\n")
            append("set +C\n")
            append("trap 'rm -f \"\$PIDFILE\"' EXIT\n")
            if (workingDir.isNotBlank()) {
                append("cd ").append(shellQuote(workingDir)).append(" || true\n")
            }
            append("exec ").append(sageCall).append('\n')
        }

        val winScript = java.nio.file.Files.createTempFile("sage-runner-", ".sh")
        winScript.toFile().deleteOnExit()
        java.nio.file.Files.writeString(
            winScript,
            script,
            Charsets.UTF_8,
            java.nio.file.StandardOpenOption.TRUNCATE_EXISTING,
        )
        val wslScript = SagemathWslSupport.toWslPath(wslDistribution, winScript.toAbsolutePath().toString())

        return GeneralCommandLine()
            .withExePath("wsl.exe")
            .withParameters("-d", wslDistribution, "--", "/bin/sh", wslScript)
            .withCharset(Charsets.UTF_8)
    }

    private fun shellQuote(s: String): String {
        if (s.isEmpty()) return "''"
        if (s.all { it.isLetterOrDigit() || it in "/_-.:,@+=" }) return s
        return "'" + s.replace("'", "'\\''") + "'"
    }

    companion object {
        private val LOG = Logger.getInstance(SagemathCommandLineState::class.java)
        private val cleanupDone = AtomicBoolean(false)

        private fun cleanupStaleTempScriptsOnce() {
            if (!cleanupDone.compareAndSet(false, true)) return
            ApplicationManager.getApplication().executeOnPooledThread {
                try {
                    val tempDir = java.nio.file.Paths.get(System.getProperty("java.io.tmpdir"))
                    val cutoff = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)
                    java.nio.file.Files.list(tempDir).use { stream ->
                        stream.forEach { path ->
                            val name = path.fileName.toString()
                            if (!name.startsWith("sage-runner-") || !name.endsWith(".sh")) return@forEach
                            try {
                                val mtime = java.nio.file.Files.getLastModifiedTime(path).toMillis()
                                if (mtime < cutoff) java.nio.file.Files.deleteIfExists(path)
                            } catch (_: Throwable) {
                                // best-effort
                            }
                        }
                    }
                } catch (t: Throwable) {
                    LOG.info("Stale sage-runner-*.sh sweep failed", t)
                }
            }
        }
    }
}
