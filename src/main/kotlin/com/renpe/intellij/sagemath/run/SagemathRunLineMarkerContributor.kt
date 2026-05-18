package com.renpe.intellij.sagemath.run

import com.intellij.execution.lineMarker.ExecutorAction
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.renpe.intellij.sagemath.lang.SagemathFileType

class SagemathRunLineMarkerContributor : RunLineMarkerContributor() {

    override fun getInfo(element: PsiElement): Info? {
        // Sage files are tokenised without a parser, so every leaf in the
        // file is a candidate. Only emit one marker per file, anchored on
        // the first non-whitespace leaf, so the gutter doesn't fill up
        // with play icons.
        if (element.firstChild != null) return null
        val file = element.containingFile ?: return null
        if (file.fileType !is SagemathFileType) return null
        if (firstMeaningfulLeaf(file) !== element) return null

        return Info(
            AllIcons.RunConfigurations.TestState.Run,
            ExecutorAction.getActions(0),
        ) { "Run SageMath script" }
    }

    private fun firstMeaningfulLeaf(file: PsiFile): PsiElement? =
        CachedValuesManager.getCachedValue(file) {
            var leaf: PsiElement? = PsiTreeUtil.getDeepestFirst(file)
            while (leaf != null && leaf.text.isBlank()) {
                leaf = PsiTreeUtil.nextLeaf(leaf)
            }
            CachedValueProvider.Result.create(leaf, file)
        }
}
