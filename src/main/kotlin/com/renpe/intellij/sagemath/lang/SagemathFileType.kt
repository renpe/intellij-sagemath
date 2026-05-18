package com.renpe.intellij.sagemath.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object SagemathIcons {
    val FILE: Icon = IconLoader.getIcon("/icons/sagemath.svg", SagemathIcons::class.java)
}

object SagemathFileType : LanguageFileType(SagemathLanguage) {
    override fun getName(): String = "SageMath"
    override fun getDescription(): String = "SageMath source file"
    override fun getDefaultExtension(): String = "sage"
    override fun getIcon(): Icon = SagemathIcons.FILE
}
