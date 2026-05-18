package com.renpe.intellij.sagemath.brace

import com.renpe.intellij.sagemath.lang.SagemathTokenTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class SagemathBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS
    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true
    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int = openingBraceOffset

    private companion object {
        val PAIRS: Array<BracePair> = arrayOf(
            BracePair(SagemathTokenTypes.LPAREN, SagemathTokenTypes.RPAREN, false),
            BracePair(SagemathTokenTypes.LBRACK, SagemathTokenTypes.RBRACK, false),
            BracePair(SagemathTokenTypes.LBRACE, SagemathTokenTypes.RBRACE, false),
        )
    }
}
