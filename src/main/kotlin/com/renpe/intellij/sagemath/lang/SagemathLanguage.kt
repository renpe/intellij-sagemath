package com.renpe.intellij.sagemath.lang

import com.intellij.lang.Language

object SagemathLanguage : Language("SageMath") {
    override fun getDisplayName(): String = "SageMath"
    private fun readResolve(): Any = SagemathLanguage
}
