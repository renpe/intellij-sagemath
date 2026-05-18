package com.renpe.intellij.sagemath.lang

import com.intellij.lang.Commenter

class SagemathCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "#"
    // Python/Sage has no real block-comment syntax. We expose triple-quoted
    // strings as the block "comment" pair so IDE actions like Comment with
    // Block Comment produce something idiomatic.
    override fun getBlockCommentPrefix(): String = "\"\"\""
    override fun getBlockCommentSuffix(): String = "\"\"\""
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
