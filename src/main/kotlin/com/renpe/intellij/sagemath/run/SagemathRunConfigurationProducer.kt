package com.renpe.intellij.sagemath.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.renpe.intellij.sagemath.lang.SagemathFileType

class SagemathRunConfigurationProducer : LazyRunConfigurationProducer<SagemathRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory =
        SagemathRunConfigurationType.getInstance().factory

    override fun setupConfigurationFromContext(
        configuration: SagemathRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val file = context.location?.virtualFile ?: return false
        if (file.fileType !is SagemathFileType) return false
        configuration.scriptPath = file.path
        configuration.name = configuration.suggestedName() ?: file.name
        return true
    }

    override fun isConfigurationFromContext(
        configuration: SagemathRunConfiguration,
        context: ConfigurationContext,
    ): Boolean {
        val file = context.location?.virtualFile ?: return false
        return file.fileType is SagemathFileType && configuration.scriptPath == file.path
    }
}
