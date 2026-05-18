package com.renpe.intellij.sagemath.lang

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object SagemathIcons {
    // PNG, not SVG: the file icon is the official SageMath icosahedron
    // avatar (https://github.com/sagemath/) with a rounded-corner clip.
    // IntelliJ picks up @2x.png automatically for HiDPI displays.
    val FILE: Icon = IconLoader.getIcon("/icons/sagemath.png", SagemathIcons::class.java)
}

object SagemathFileType : LanguageFileType(SagemathLanguage) {
    override fun getName(): String = "SageMath"
    override fun getDescription(): String = "SageMath source file"
    override fun getDefaultExtension(): String = "sage"
    override fun getIcon(): Icon = SagemathIcons.FILE
}
