package com.renpe.intellij.sagemath.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.renpe.intellij.sagemath.lang.SagemathIcons
import javax.swing.Icon

class SagemathRunConfigurationType : ConfigurationType {
    val factory = SagemathRunConfigurationFactory(this)

    override fun getDisplayName(): String = "SageMath"
    override fun getConfigurationTypeDescription(): String = "SageMath script run configuration"
    override fun getIcon(): Icon = SagemathIcons.FILE
    override fun getId(): String = "SagemathRunConfiguration"
    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(factory)

    companion object {
        fun getInstance(): SagemathRunConfigurationType =
            ConfigurationTypeUtil.findConfigurationType(SagemathRunConfigurationType::class.java)
    }
}
