package com.renpe.intellij.sagemath.lang

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

/**
 * Sage is Python with a preparser, so the lexer is essentially a Python
 * lexer plus a few Sage-only constructs:
 *
 *  * `R.<x, y> = PolynomialRing(QQ)` — the angle-bracket generator
 *    declaration that the preparser rewrites into `_first_ngens`.
 *  * Top-of-line `%time`, `%timeit`, … magics from the Sage REPL.
 *  * `^` as a power operator (the preparser rewrites it to `**`), but at
 *    the lexer level it is just another OPERATOR token.
 *
 * We don't try to track Python's indentation; highlighting works fine
 * with a flat token stream.
 */
class SagemathLexer : LexerBase() {

    private var buffer: CharSequence = ""
    private var startOffset = 0
    private var endOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null
    private var state = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.tokenType = null
        this.state = initialState
        advance()
    }

    override fun getState(): Int = state
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= endOffset) {
            tokenType = null
            return
        }
        val c = buffer[tokenStart]
        tokenType = when {
            c == '\n' || c == '\r' -> readNewline()
            c.isWhitespace() -> readWhitespace()
            c == '#' -> readLineComment()
            isPrefixedStringStart(c) -> readPrefixedString()
            c == '"' || c == '\'' -> readString(c)
            atLineStart() && c == '%' && isIdentStart(peek(1)) -> readMagic()
            c == '@' && isIdentStart(peek(1)) -> readDecorator()
            c.isDigit() -> readNumber()
            c == '.' && peek(1)?.isDigit() == true -> readNumber()
            c == '<' && isGeneratorDeclContext() -> readGeneratorDecl()
            isIdentStart(c) -> readIdentifierOrKeyword()
            else -> readPunctuationOrOperator()
        }
    }

    // ----- helpers ---------------------------------------------------

    private fun peek(offset: Int): Char? {
        val p = tokenEnd + offset
        return if (p in 0 until endOffset) buffer[p] else null
    }

    private fun isIdentStart(c: Char?): Boolean =
        c != null && (c.isLetter() || c == '_')

    private fun isIdentPart(c: Char?): Boolean =
        c != null && (c.isLetterOrDigit() || c == '_')

    /** Single-char Python string prefix: r/b/u/f. */
    private fun isStringPrefix(c: Char): Boolean = c in "rRbBuUfF"

    /**
     * True when the next 1–2 chars are a string prefix immediately
     * followed by a quote. Handles `r"..."`, `rb"..."`, `br"..."`,
     * `fr"..."`, etc.
     */
    private fun isPrefixedStringStart(c: Char): Boolean {
        if (!isStringPrefix(c)) return false
        val n1 = peek(1) ?: return false
        if (n1 == '"' || n1 == '\'') return true
        if (!isStringPrefix(n1)) return false
        val n2 = peek(2) ?: return false
        return n2 == '"' || n2 == '\''
    }

    private fun atLineStart(): Boolean {
        if (tokenStart == 0) return true
        var i = tokenStart - 1
        while (i >= 0) {
            val ch = buffer[i]
            if (ch == '\n' || ch == '\r') return true
            if (!ch.isWhitespace()) return false
            i--
        }
        return true
    }

    /**
     * True when the `<` at [tokenEnd] looks like a Sage generator
     * declaration: it is preceded directly by `.`, which is preceded by
     * an identifier character, and it is followed by an identifier.
     *
     *     R.<x, y> = PolynomialRing(QQ)
     *      ^ tokenEnd
     */
    private fun isGeneratorDeclContext(): Boolean {
        if (tokenStart == 0) return false
        if (buffer[tokenStart - 1] != '.') return false
        if (tokenStart < 2) return false
        if (!isIdentPart(buffer[tokenStart - 2])) return false
        val after = peek(1) ?: return false
        return isIdentStart(after) || after.isWhitespace()
    }

    // ----- token readers --------------------------------------------

    private fun readNewline(): IElementType {
        tokenEnd++
        if (tokenEnd < endOffset && buffer[tokenEnd] == '\n' && buffer[tokenStart] == '\r') {
            tokenEnd++
        }
        return SagemathTokenTypes.WHITE_SPACE
    }

    private fun readWhitespace(): IElementType {
        while (tokenEnd < endOffset) {
            val ch = buffer[tokenEnd]
            if (ch == '\n' || ch == '\r' || !ch.isWhitespace()) break
            tokenEnd++
        }
        return SagemathTokenTypes.WHITE_SPACE
    }

    private fun readLineComment(): IElementType {
        while (tokenEnd < endOffset && buffer[tokenEnd] != '\n') tokenEnd++
        return SagemathTokenTypes.LINE_COMMENT
    }

    private fun readPrefixedString(): IElementType {
        // Consume 1-2 prefix chars, then dispatch to readString.
        tokenEnd++ // first prefix char
        if (tokenEnd < endOffset && isStringPrefix(buffer[tokenEnd])) tokenEnd++
        val quote = buffer[tokenEnd]
        return readString(quote)
    }

    private fun readString(quote: Char): IElementType {
        // Triple-quoted?
        val triple = tokenEnd + 2 < endOffset &&
                buffer[tokenEnd] == quote &&
                buffer[tokenEnd + 1] == quote &&
                buffer[tokenEnd + 2] == quote
        if (triple) {
            tokenEnd += 3
            while (tokenEnd < endOffset) {
                if (tokenEnd + 2 < endOffset &&
                    buffer[tokenEnd] == quote &&
                    buffer[tokenEnd + 1] == quote &&
                    buffer[tokenEnd + 2] == quote
                ) {
                    tokenEnd += 3
                    return SagemathTokenTypes.STRING_TRIPLE
                }
                if (buffer[tokenEnd] == '\\' && tokenEnd + 1 < endOffset) {
                    tokenEnd += 2
                } else {
                    tokenEnd++
                }
            }
            return SagemathTokenTypes.STRING_TRIPLE
        }
        tokenEnd++ // opening quote
        while (tokenEnd < endOffset) {
            val ch = buffer[tokenEnd]
            if (ch == '\\' && tokenEnd + 1 < endOffset) {
                tokenEnd += 2
                continue
            }
            if (ch == quote) {
                tokenEnd++
                return if (quote == '"') SagemathTokenTypes.STRING_DOUBLE
                else SagemathTokenTypes.STRING_SINGLE
            }
            if (ch == '\n') {
                return if (quote == '"') SagemathTokenTypes.STRING_DOUBLE
                else SagemathTokenTypes.STRING_SINGLE
            }
            tokenEnd++
        }
        return if (quote == '"') SagemathTokenTypes.STRING_DOUBLE
        else SagemathTokenTypes.STRING_SINGLE
    }

    private fun readMagic(): IElementType {
        tokenEnd++ // the %
        // Double-percent magics like %%cython
        if (tokenEnd < endOffset && buffer[tokenEnd] == '%') tokenEnd++
        while (tokenEnd < endOffset && isIdentPart(buffer[tokenEnd])) tokenEnd++
        return SagemathTokenTypes.MAGIC
    }

    private fun readDecorator(): IElementType {
        tokenEnd++ // the @
        while (tokenEnd < endOffset && (isIdentPart(buffer[tokenEnd]) || buffer[tokenEnd] == '.')) {
            tokenEnd++
        }
        return SagemathTokenTypes.DECORATOR
    }

    private fun readNumber(): IElementType {
        // Hex / octal / binary
        if (buffer[tokenEnd] == '0' && tokenEnd + 1 < endOffset) {
            val nxt = buffer[tokenEnd + 1]
            if (nxt == 'x' || nxt == 'X') {
                tokenEnd += 2
                while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() ||
                            buffer[tokenEnd] in 'a'..'f' || buffer[tokenEnd] in 'A'..'F' ||
                            buffer[tokenEnd] == '_')
                ) tokenEnd++
                consumeNumberSuffix()
                return SagemathTokenTypes.NUMBER
            }
            if (nxt == 'o' || nxt == 'O') {
                tokenEnd += 2
                while (tokenEnd < endOffset && (buffer[tokenEnd] in '0'..'7' || buffer[tokenEnd] == '_')) tokenEnd++
                consumeNumberSuffix()
                return SagemathTokenTypes.NUMBER
            }
            if (nxt == 'b' || nxt == 'B') {
                tokenEnd += 2
                while (tokenEnd < endOffset && (buffer[tokenEnd] == '0' || buffer[tokenEnd] == '1' || buffer[tokenEnd] == '_')) tokenEnd++
                consumeNumberSuffix()
                return SagemathTokenTypes.NUMBER
            }
        }
        while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd] == '_')) tokenEnd++
        if (tokenEnd < endOffset && buffer[tokenEnd] == '.' &&
            (tokenEnd + 1 >= endOffset || buffer[tokenEnd + 1] != '.')
        ) {
            tokenEnd++
            while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd] == '_')) tokenEnd++
        }
        if (tokenEnd < endOffset && (buffer[tokenEnd] == 'e' || buffer[tokenEnd] == 'E')) {
            tokenEnd++
            if (tokenEnd < endOffset && (buffer[tokenEnd] == '+' || buffer[tokenEnd] == '-')) tokenEnd++
            while (tokenEnd < endOffset && (buffer[tokenEnd].isDigit() || buffer[tokenEnd] == '_')) tokenEnd++
        }
        consumeNumberSuffix()
        return SagemathTokenTypes.NUMBER
    }

    private fun consumeNumberSuffix() {
        // Python imaginary literal: j/J. Sage also accepts the preparser's
        // own integer suffixes (r for "raw Python int", but those are
        // rewritten on input — we just swallow them too).
        while (tokenEnd < endOffset && buffer[tokenEnd] in "jJlLrR") tokenEnd++
    }

    private fun readGeneratorDecl(): IElementType {
        tokenEnd++ // the <
        var depth = 1
        while (tokenEnd < endOffset && depth > 0) {
            val ch = buffer[tokenEnd]
            if (ch == '\n') break
            if (ch == '<') depth++
            else if (ch == '>') depth--
            tokenEnd++
            if (depth == 0) break
        }
        return SagemathTokenTypes.GENERATOR_DECL
    }

    private fun readIdentifierOrKeyword(): IElementType {
        val start = tokenEnd
        while (tokenEnd < endOffset && isIdentPart(buffer[tokenEnd])) tokenEnd++
        val word = buffer.subSequence(start, tokenEnd).toString()

        return when (word) {
            in SagemathKeywords.CONSTANT -> SagemathTokenTypes.CONSTANT
            in SagemathKeywords.CONTROL -> SagemathTokenTypes.KEYWORD_CONTROL
            in SagemathKeywords.DECL -> SagemathTokenTypes.KEYWORD_DECL
            in SagemathKeywords.STATEMENT -> SagemathTokenTypes.KEYWORD_STATEMENT
            in SagemathKeywords.OPERATOR_WORD -> SagemathTokenTypes.KEYWORD_OPERATOR_WORD
            in SagemathKeywords.SELF -> SagemathTokenTypes.SELF_PARAM
            in SagemathKeywords.TYPE_CATEGORY -> SagemathTokenTypes.TYPE_CATEGORY
            in SagemathKeywords.BUILTIN_FUNCTION -> SagemathTokenTypes.BUILTIN_FUNCTION
            else -> SagemathTokenTypes.IDENTIFIER
        }
    }

    private fun readPunctuationOrOperator(): IElementType {
        val c = buffer[tokenEnd]
        val two = if (tokenEnd + 1 < endOffset) "$c${buffer[tokenEnd + 1]}" else null
        val three = if (tokenEnd + 2 < endOffset) "$two${buffer[tokenEnd + 2]}" else null
        when (three) {
            "**=", "//=", ">>=", "<<=", "...", "->>" -> {
                tokenEnd += 3
                return if (three == "...") SagemathTokenTypes.OPERATOR else SagemathTokenTypes.ASSIGN
            }
        }
        when (two) {
            ":=" -> { tokenEnd += 2; return SagemathTokenTypes.ASSIGN }
            "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "@=" -> {
                tokenEnd += 2; return SagemathTokenTypes.ASSIGN
            }
            "==", "!=", "<=", ">=", "<<", ">>", "**", "//", "->" -> {
                tokenEnd += 2; return SagemathTokenTypes.OPERATOR
            }
        }
        tokenEnd++
        return when (c) {
            '(' -> SagemathTokenTypes.LPAREN
            ')' -> SagemathTokenTypes.RPAREN
            '[' -> SagemathTokenTypes.LBRACK
            ']' -> SagemathTokenTypes.RBRACK
            '{' -> SagemathTokenTypes.LBRACE
            '}' -> SagemathTokenTypes.RBRACE
            ';' -> SagemathTokenTypes.SEMICOLON
            ',' -> SagemathTokenTypes.COMMA
            '.' -> SagemathTokenTypes.DOT
            ':' -> SagemathTokenTypes.COLON
            '=' -> SagemathTokenTypes.ASSIGN
            '+', '-', '*', '/', '%', '&', '|', '^', '<', '>',
            '!', '~', '?' -> SagemathTokenTypes.OPERATOR
            else -> SagemathTokenTypes.BAD_CHARACTER
        }
    }
}
