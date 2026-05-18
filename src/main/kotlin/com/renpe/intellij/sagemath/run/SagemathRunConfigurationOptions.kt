package com.renpe.intellij.sagemath.run

import com.intellij.execution.configurations.LocatableRunConfigurationOptions

class SagemathRunConfigurationOptions : LocatableRunConfigurationOptions() {
    var scriptPath: String? by string()
    var scriptArgs: String? by string()
    var workingDirectory: String? by string()
    var pythonMode: Boolean by property(false)
    var preparseOnly: Boolean by property(false)
    var extraArgs: String? by string()
}
