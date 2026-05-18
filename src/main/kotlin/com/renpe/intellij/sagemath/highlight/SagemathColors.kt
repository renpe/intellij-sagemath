package com.renpe.intellij.sagemath.highlight

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

object SagemathColors {
    val LINE_COMMENT = key("SAGEMATH_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    val STRING = key("SAGEMATH_STRING", DefaultLanguageHighlighterColors.STRING)
    val STRING_TRIPLE = key("SAGEMATH_STRING_TRIPLE", DefaultLanguageHighlighterColors.DOC_COMMENT)
    val NUMBER = key("SAGEMATH_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
    val IDENTIFIER = key("SAGEMATH_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)

    val KEYWORD_CONTROL = key("SAGEMATH_KEYWORD_CONTROL", DefaultLanguageHighlighterColors.KEYWORD)
    val KEYWORD_DECL = key("SAGEMATH_KEYWORD_DECL", DefaultLanguageHighlighterColors.KEYWORD)
    val KEYWORD_STATEMENT = key("SAGEMATH_KEYWORD_STATEMENT", DefaultLanguageHighlighterColors.KEYWORD)
    val KEYWORD_OPERATOR_WORD = key("SAGEMATH_KEYWORD_OPERATOR_WORD", DefaultLanguageHighlighterColors.KEYWORD)
    val CONSTANT = key("SAGEMATH_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
    val SELF_PARAM = key("SAGEMATH_SELF_PARAM", DefaultLanguageHighlighterColors.KEYWORD)
    val TYPE_CATEGORY = key("SAGEMATH_TYPE_CATEGORY", DefaultLanguageHighlighterColors.CLASS_NAME)
    val BUILTIN_FUNCTION = key("SAGEMATH_BUILTIN_FUNCTION", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)

    val MAGIC = key("SAGEMATH_MAGIC", DefaultLanguageHighlighterColors.METADATA)
    val DECORATOR = key("SAGEMATH_DECORATOR", DefaultLanguageHighlighterColors.METADATA)
    val GENERATOR_DECL = key("SAGEMATH_GENERATOR_DECL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)

    val OPERATOR = key("SAGEMATH_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val PARENTHESES = key("SAGEMATH_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
    val BRACKETS = key("SAGEMATH_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
    val BRACES = key("SAGEMATH_BRACES", DefaultLanguageHighlighterColors.BRACES)
    val SEMICOLON = key("SAGEMATH_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
    val COMMA = key("SAGEMATH_COMMA", DefaultLanguageHighlighterColors.COMMA)
    val DOT = key("SAGEMATH_DOT", DefaultLanguageHighlighterColors.DOT)

    val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
        "SAGEMATH_BAD_CHARACTER",
        com.intellij.openapi.editor.HighlighterColors.BAD_CHARACTER,
    )

    private fun key(name: String, fallback: TextAttributesKey): TextAttributesKey =
        TextAttributesKey.createTextAttributesKey(name, fallback)
}
