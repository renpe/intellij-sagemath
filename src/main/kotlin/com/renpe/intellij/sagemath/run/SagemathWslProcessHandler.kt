package com.renpe.intellij.sagemath.run

import com.intellij.execution.process.KillableProcessHandler
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * KillableProcessHandler for sage running inside WSL.
 *
 * `wsl.exe` with redirected stdio does NOT propagate SIGTERM to its Linux
 * children (Microsoft/WSL#3766, #2496), so the Windows-side wrapper dying
 * leaves sage running, adopted by /init. Our shell wrapper writes its own
 * PID to [pidFilePath] right before `exec`ing into sage — POSIX guarantees
 * the PID survives exec(2). On stop we open a second wsl.exe and signal that
 * PID directly. We also negate the PID so the signal reaches the whole
 * process group (sage forks several subprocesses).
 */
class SagemathWslProcessHandler(
    process: Process,
    displayCommandLine: String,
    charset: Charset,
    private val distribution: String,
    private val pidFilePath: String,
) : KillableProcessHandler(process, displayCommandLine, charset) {

    init {
        setShouldDestroyProcessRecursively(true)
    }

    override fun destroyProcessImpl() {
        if (distribution.isNotBlank() && pidFilePath.isNotBlank()) {
            ApplicationManager.getApplication().executeOnPooledThread {
                killWslSideProcess()
            }
        }
        super.destroyProcessImpl()
    }

    private fun killWslSideProcess() {
        // Sage spawns child processes (the Python interpreter, GAP, Pari…),
        // so we send the signal to the whole process group with kill -GROUP.
        val script = "i=0; " +
            "while [ \$i -lt 10 ] && [ ! -s '$pidFilePath' ]; do sleep 0.05; i=\$((i+1)); done; " +
            "pid=\$(cat '$pidFilePath' 2>/dev/null) || pid=''; " +
            "if [ -n \"\$pid\" ]; then " +
            "  kill -TERM -\"\$pid\" 2>/dev/null || kill -TERM \"\$pid\" 2>/dev/null; " +
            "  for i in 1 2 3 4 5; do kill -0 \"\$pid\" 2>/dev/null || break; sleep 0.3; done; " +
            "  kill -KILL -\"\$pid\" 2>/dev/null || kill -KILL \"\$pid\" 2>/dev/null; " +
            "fi; " +
            "rm -f '$pidFilePath' 2>/dev/null"

        try {
            val pb = ProcessBuilder("wsl.exe", "-d", distribution, "--", "sh", "-c", script)
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .redirectError(ProcessBuilder.Redirect.DISCARD)
            val process = pb.start()
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly()
                LOG.warn("Timed out waiting for WSL-side kill of sage (pidfile=$pidFilePath)")
            } else {
                LOG.info("WSL-side kill finished with exit=${process.exitValue()}")
            }
        } catch (t: Throwable) {
            LOG.warn("Failed to run WSL-side kill for sage (pidfile=$pidFilePath)", t)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(SagemathWslProcessHandler::class.java)
    }
}
