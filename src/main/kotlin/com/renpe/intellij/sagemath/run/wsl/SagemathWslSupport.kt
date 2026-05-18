package com.renpe.intellij.sagemath.run.wsl

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

/**
 * Thin wrapper around the WSL platform API.
 *
 * All WSL classes are loaded reflectively so the plugin still loads on
 * IDEs/platforms where the WSL implementation classes are missing or fail
 * to initialize (older builds, non-Windows, headless verifier sandboxes).
 */
object SagemathWslSupport {

    private val LOG = Logger.getInstance(SagemathWslSupport::class.java)

    fun isSupported(): Boolean = SystemInfo.isWindows && loadManager() != null

    fun listDistributions(): List<String> {
        val manager = loadManager() ?: return emptyList()
        return try {
            @Suppress("UNCHECKED_CAST")
            val installed = manager.javaClass.getMethod("getInstalledDistributions")
                .invoke(manager) as List<Any>
            installed.mapNotNull { dist ->
                runCatching {
                    dist.javaClass.getMethod("getMsId").invoke(dist) as? String
                }.getOrNull()
            }
        } catch (t: Throwable) {
            LOG.info("Failed to enumerate WSL distributions", t)
            emptyList()
        }
    }

    /**
     * Converts a host (Windows) path to its WSL representation
     * (`C:\foo\bar` → `/mnt/c/foo/bar`). Returns the original path if no
     * conversion is possible.
     */
    fun toWslPath(distributionMsId: String, hostPath: String): String {
        if (hostPath.isBlank()) return hostPath
        val distribution = findDistribution(distributionMsId) ?: return hostPath
        return try {
            (distribution.javaClass.getMethod("getWslPath", String::class.java)
                .invoke(distribution, hostPath) as? String) ?: hostPath
        } catch (_: NoSuchMethodException) {
            try {
                val path = java.nio.file.Paths.get(hostPath)
                (distribution.javaClass.getMethod("getWslPath", java.nio.file.Path::class.java)
                    .invoke(distribution, path) as? String) ?: hostPath
            } catch (t: Throwable) {
                LOG.info("getWslPath fallback failed for '$hostPath'", t)
                hostPath
            }
        } catch (t: Throwable) {
            LOG.info("getWslPath failed for '$hostPath'", t)
            hostPath
        }
    }

    /**
     * Wraps `commandLine` so it executes inside the given WSL distribution.
     * If the distribution cannot be resolved or the platform call fails the
     * original command line is returned unchanged.
     */
    fun patchCommandLine(
        commandLine: GeneralCommandLine,
        project: Project?,
        distributionMsId: String,
    ): GeneralCommandLine {
        val distribution = findDistribution(distributionMsId) ?: return commandLine
        return try {
            val optsClass = Class.forName("com.intellij.execution.wsl.WSLCommandLineOptions")
            val options = optsClass.getConstructor().newInstance()
            val method = distribution.javaClass.getMethod(
                "patchCommandLine",
                GeneralCommandLine::class.java,
                Project::class.java,
                optsClass,
            )
            @Suppress("UNCHECKED_CAST")
            method.invoke(distribution, commandLine, project, options) as GeneralCommandLine
        } catch (t: Throwable) {
            LOG.warn("WSL patchCommandLine failed; running natively", t)
            commandLine
        }
    }

    private fun findDistribution(msId: String): Any? {
        if (msId.isBlank()) return null
        val manager = loadManager() ?: return null
        return try {
            @Suppress("UNCHECKED_CAST")
            val installed = manager.javaClass.getMethod("getInstalledDistributions")
                .invoke(manager) as List<Any>
            installed.firstOrNull { dist ->
                runCatching {
                    (dist.javaClass.getMethod("getMsId").invoke(dist) as? String) == msId
                }.getOrDefault(false)
            }
        } catch (t: Throwable) {
            LOG.info("Failed to look up WSL distribution '$msId'", t)
            null
        }
    }

    private fun loadManager(): Any? = try {
        val managerClass = Class.forName("com.intellij.execution.wsl.WslDistributionManager")
        managerClass.getMethod("getInstance").invoke(null)
    } catch (_: ClassNotFoundException) {
        null
    } catch (t: Throwable) {
        LOG.info("WslDistributionManager unavailable", t)
        null
    }
}
