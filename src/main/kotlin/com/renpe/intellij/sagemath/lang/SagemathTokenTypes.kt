package com.renpe.intellij.sagemath.lang

import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class SagemathTokenType(debugName: String) : IElementType(debugName, SagemathLanguage)

object SagemathTokenTypes {
    val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE
    val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    val LINE_COMMENT = SagemathTokenType("LINE_COMMENT")

    val STRING_DOUBLE = SagemathTokenType("STRING_DOUBLE")
    val STRING_SINGLE = SagemathTokenType("STRING_SINGLE")
    val STRING_TRIPLE = SagemathTokenType("STRING_TRIPLE")
    val NUMBER = SagemathTokenType("NUMBER")

    val IDENTIFIER = SagemathTokenType("IDENTIFIER")

    // Sage preparser sugar: R.<x, y> = PolynomialRing(QQ) — the <x, y> part
    // is a generator declaration and we want to highlight it distinctly.
    val GENERATOR_DECL = SagemathTokenType("GENERATOR_DECL")

    // Magic line prefix: %time, %timeit, %display, %attach, %load — Sage REPL
    // magics that are valid only at the start of a line.
    val MAGIC = SagemathTokenType("MAGIC")

    val KEYWORD_CONTROL = SagemathTokenType("KEYWORD_CONTROL")
    val KEYWORD_DECL = SagemathTokenType("KEYWORD_DECL")
    val KEYWORD_STATEMENT = SagemathTokenType("KEYWORD_STATEMENT")
    val KEYWORD_OPERATOR_WORD = SagemathTokenType("KEYWORD_OPERATOR_WORD")
    val CONSTANT = SagemathTokenType("CONSTANT")
    val SELF_PARAM = SagemathTokenType("SELF_PARAM")
    val TYPE_CATEGORY = SagemathTokenType("TYPE_CATEGORY")
    val BUILTIN_FUNCTION = SagemathTokenType("BUILTIN_FUNCTION")

    val OPERATOR = SagemathTokenType("OPERATOR")
    val ASSIGN = SagemathTokenType("ASSIGN")

    val LPAREN = SagemathTokenType("LPAREN")
    val RPAREN = SagemathTokenType("RPAREN")
    val LBRACK = SagemathTokenType("LBRACK")
    val RBRACK = SagemathTokenType("RBRACK")
    val LBRACE = SagemathTokenType("LBRACE")
    val RBRACE = SagemathTokenType("RBRACE")
    val SEMICOLON = SagemathTokenType("SEMICOLON")
    val COMMA = SagemathTokenType("COMMA")
    val DOT = SagemathTokenType("DOT")
    val COLON = SagemathTokenType("COLON")

    val DECORATOR = SagemathTokenType("DECORATOR")
}
