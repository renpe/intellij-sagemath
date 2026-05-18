package com.renpe.intellij.sagemath.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationWithSuppressedDefaultDebugAction
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class SagemathRunConfiguration(
    project: Project,
    factory: ConfigurationFactory,
    name: String,
) : LocatableConfigurationBase<SagemathRunConfigurationOptions>(project, factory, name),
    RunConfigurationWithSuppressedDefaultDebugAction {

    private val opts: SagemathRunConfigurationOptions
        get() = options as SagemathRunConfigurationOptions

    var scriptPath: String
        get() = opts.scriptPath.orEmpty()
        set(value) { opts.scriptPath = value }

    var scriptArgs: String
        get() = opts.scriptArgs.orEmpty()
        set(value) { opts.scriptArgs = value }

    var workingDirectory: String
        get() = opts.workingDirectory.orEmpty()
        set(value) { opts.workingDirectory = value }

    var pythonMode: Boolean
        get() = opts.pythonMode
        set(value) { opts.pythonMode = value }

    var preparseOnly: Boolean
        get() = opts.preparseOnly
        set(value) { opts.preparseOnly = value }

    var extraArgs: String
        get() = opts.extraArgs.orEmpty()
        set(value) { opts.extraArgs = value }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
        SagemathRunSettingsEditor(project)

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        if (scriptPath.isBlank()) {
            throw ExecutionException("No SageMath script selected. Edit the run configuration and pick a .sage file.")
        }
        return SagemathCommandLineState(this, environment)
    }

    override fun suggestedName(): String? {
        val path = scriptPath.takeIf { it.isNotBlank() } ?: return null
        return path.substringAfterLast('/').substringAfterLast('\\').ifEmpty { null }
    }
}
