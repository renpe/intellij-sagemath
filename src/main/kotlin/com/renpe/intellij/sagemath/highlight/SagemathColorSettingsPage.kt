package com.renpe.intellij.sagemath.highlight

import com.renpe.intellij.sagemath.lang.SagemathIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class SagemathColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon = SagemathIcons.FILE
    override fun getHighlighter(): SyntaxHighlighter = SagemathSyntaxHighlighter()
    override fun getDisplayName(): String = "SageMath"
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = emptyMap()

    override fun getDemoText(): String = """
        # SageMath sample
        '''
        Compute the rank of an elliptic curve and play with a polynomial ring.
        '''
        from sage.all import *

        @cached_function
        def rank_of(a, b):
            E = EllipticCurve([a, b])
            return E.rank()

        R.<x, y> = PolynomialRing(QQ)
        f = x^3 + y^2 - 1
        g = f.factor()

        for p in primes(100):
            if is_prime(p^2 + 1):
                print(p, "→", factor(p^2 + 1))

        K.<a> = NumberField(x^2 - 2)
        M = Matrix(K, [[1, a], [a, 1]])
        assert M.det() == 1 - 2

        %time _ = factorial(10000)
    """.trimIndent()

    private companion object {
        val DESCRIPTORS: Array<AttributesDescriptor> = arrayOf(
            AttributesDescriptor("Comments//Line comment", SagemathColors.LINE_COMMENT),
            AttributesDescriptor("Strings//String", SagemathColors.STRING),
            AttributesDescriptor("Strings//Triple-quoted string (docstring)", SagemathColors.STRING_TRIPLE),
            AttributesDescriptor("Numbers", SagemathColors.NUMBER),
            AttributesDescriptor("Identifiers//Identifier", SagemathColors.IDENTIFIER),
            AttributesDescriptor("Keywords//Control flow (if, for, while, …)", SagemathColors.KEYWORD_CONTROL),
            AttributesDescriptor("Keywords//Declaration (def, class, lambda, …)", SagemathColors.KEYWORD_DECL),
            AttributesDescriptor("Keywords//Statement (import, del, print, …)", SagemathColors.KEYWORD_STATEMENT),
            AttributesDescriptor("Keywords//Operator word (and, or, not, in, is)", SagemathColors.KEYWORD_OPERATOR_WORD),
            AttributesDescriptor("Constants (True, False, None, pi, e, oo, …)", SagemathColors.CONSTANT),
            AttributesDescriptor("Identifiers//self/cls parameter", SagemathColors.SELF_PARAM),
            AttributesDescriptor("Type category (Integer, Matrix, EllipticCurve, …)", SagemathColors.TYPE_CATEGORY),
            AttributesDescriptor("Built-in function (factor, plot, integrate, …)", SagemathColors.BUILTIN_FUNCTION),
            AttributesDescriptor("Sage magic (%time, %timeit, …)", SagemathColors.MAGIC),
            AttributesDescriptor("Decorator (@name)", SagemathColors.DECORATOR),
            AttributesDescriptor("Generator declaration (.<x, y>)", SagemathColors.GENERATOR_DECL),
            AttributesDescriptor("Operators//Operator sign", SagemathColors.OPERATOR),
            AttributesDescriptor("Operators//Parentheses", SagemathColors.PARENTHESES),
            AttributesDescriptor("Operators//Brackets", SagemathColors.BRACKETS),
            AttributesDescriptor("Operators//Braces", SagemathColors.BRACES),
            AttributesDescriptor("Operators//Semicolon", SagemathColors.SEMICOLON),
            AttributesDescriptor("Operators//Comma", SagemathColors.COMMA),
            AttributesDescriptor("Operators//Dot", SagemathColors.DOT),
            AttributesDescriptor("Bad character", SagemathColors.BAD_CHARACTER),
        )
    }
}
