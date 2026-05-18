package com.renpe.intellij.sagemath.template

import com.renpe.intellij.sagemath.lang.SagemathFileType
import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class SagemathTemplateContextType : TemplateContextType("SageMath") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        val file = templateActionContext.file
        return file.fileType == SagemathFileType
    }
}
