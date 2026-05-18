package com.renpe.intellij.sagemath.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class SagemathRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String = "SagemathRunConfigurationFactory"

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        SagemathRunConfiguration(project, this, "SageMath")

    override fun getOptionsClass(): Class<out BaseState> = SagemathRunConfigurationOptions::class.java
}
