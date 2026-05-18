package com.renpe.intellij.sagemath.highlight

import com.renpe.intellij.sagemath.lang.SagemathLexer
import com.renpe.intellij.sagemath.lang.SagemathTokenTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class SagemathSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = SagemathLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val key = ATTR_MAP[tokenType] ?: return TextAttributesKey.EMPTY_ARRAY
        return arrayOf(key)
    }

    private companion object {
        val ATTR_MAP: Map<IElementType, TextAttributesKey> = mapOf(
            SagemathTokenTypes.LINE_COMMENT to SagemathColors.LINE_COMMENT,
            SagemathTokenTypes.STRING_DOUBLE to SagemathColors.STRING,
            SagemathTokenTypes.STRING_SINGLE to SagemathColors.STRING,
            SagemathTokenTypes.STRING_TRIPLE to SagemathColors.STRING_TRIPLE,
            SagemathTokenTypes.NUMBER to SagemathColors.NUMBER,
            SagemathTokenTypes.IDENTIFIER to SagemathColors.IDENTIFIER,
            SagemathTokenTypes.KEYWORD_CONTROL to SagemathColors.KEYWORD_CONTROL,
            SagemathTokenTypes.KEYWORD_DECL to SagemathColors.KEYWORD_DECL,
            SagemathTokenTypes.KEYWORD_STATEMENT to SagemathColors.KEYWORD_STATEMENT,
            SagemathTokenTypes.KEYWORD_OPERATOR_WORD to SagemathColors.KEYWORD_OPERATOR_WORD,
            SagemathTokenTypes.CONSTANT to SagemathColors.CONSTANT,
            SagemathTokenTypes.SELF_PARAM to SagemathColors.SELF_PARAM,
            SagemathTokenTypes.TYPE_CATEGORY to SagemathColors.TYPE_CATEGORY,
            SagemathTokenTypes.BUILTIN_FUNCTION to SagemathColors.BUILTIN_FUNCTION,
            SagemathTokenTypes.MAGIC to SagemathColors.MAGIC,
            SagemathTokenTypes.DECORATOR to SagemathColors.DECORATOR,
            SagemathTokenTypes.GENERATOR_DECL to SagemathColors.GENERATOR_DECL,
            SagemathTokenTypes.OPERATOR to SagemathColors.OPERATOR,
            SagemathTokenTypes.ASSIGN to SagemathColors.OPERATOR,
            SagemathTokenTypes.LPAREN to SagemathColors.PARENTHESES,
            SagemathTokenTypes.RPAREN to SagemathColors.PARENTHESES,
            SagemathTokenTypes.LBRACK to SagemathColors.BRACKETS,
            SagemathTokenTypes.RBRACK to SagemathColors.BRACKETS,
            SagemathTokenTypes.LBRACE to SagemathColors.BRACES,
            SagemathTokenTypes.RBRACE to SagemathColors.BRACES,
            SagemathTokenTypes.SEMICOLON to SagemathColors.SEMICOLON,
            SagemathTokenTypes.COMMA to SagemathColors.COMMA,
            SagemathTokenTypes.DOT to SagemathColors.DOT,
            SagemathTokenTypes.BAD_CHARACTER to SagemathColors.BAD_CHARACTER,
        )
    }
}
