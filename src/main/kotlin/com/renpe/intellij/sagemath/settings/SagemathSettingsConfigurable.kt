package com.renpe.intellij.sagemath.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import com.renpe.intellij.sagemath.run.wsl.SagemathWslSupport
import javax.swing.JComboBox
import javax.swing.JComponent

class SagemathSettingsConfigurable : Configurable {

    private val interpreterField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            null,
            FileChooserDescriptorFactory.singleFile()
                .withTitle("Select SageMath Executable")
                .withDescription("Path to the sage binary"),
        )
    }
    private val wslCombo: JComboBox<String> = JComboBox<String>().apply {
        addItem("")
        SagemathWslSupport.listDistributions().forEach { addItem(it) }
    }
    private val condaEnvField = JBTextField()
    private val condaPathField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            null,
            FileChooserDescriptorFactory.singleFile()
                .withTitle("Select Conda Executable")
                .withDescription("Path to the conda binary (optional)"),
        )
    }
    private val extraArgsField = JBTextField()

    override fun getDisplayName(): String = "SageMath"

    override fun createComponent(): JComponent = panel {
        row("SageMath interpreter:") {
            cell(interpreterField).align(AlignX.FILL)
                .comment(
                    "Absolute path to the <code>sage</code> binary. When a WSL distribution " +
                        "is selected, this must be the path <i>inside</i> WSL " +
                        "(e.g. <code>/usr/bin/sage</code>)."
                )
        }
        if (SystemInfo.isWindows) {
            row("Run via WSL:") {
                cell(wslCombo)
                    .comment("Optional. Pick a WSL distribution to run sage there instead of natively.")
            }
        }
        row("Conda environment:") {
            cell(condaEnvField).align(AlignX.FILL)
                .comment(
                    "Optional. When set, the run wraps the sage call in " +
                        "<code>conda run -n &lt;env&gt; --no-capture-output --live-stream sage …</code> " +
                        "so that <code>$" + "CONDA_PREFIX</code> and the env's <code>PATH</code> are " +
                        "active. Required for SageMath installed via conda/miniforge."
                )
        }
        row("Conda binary:") {
            cell(condaPathField).align(AlignX.FILL)
                .comment(
                    "Optional. Path to <code>conda</code>. Empty = inferred from the interpreter path " +
                        "(<code>&lt;root&gt;/envs/&lt;env&gt;/bin/sage</code> → <code>&lt;root&gt;/bin/conda</code>), " +
                        "falling back to <code>conda</code> on <code>PATH</code>."
                )
        }
        row("Extra arguments:") {
            cell(extraArgsField).align(AlignX.FILL)
                .comment("Passed verbatim to sage before the script path.")
        }
    }

    override fun isModified(): Boolean {
        val s = SagemathSettings.getInstance().state
        return interpreterField.text != s.interpreterPath ||
            (wslCombo.selectedItem as? String ?: "") != s.defaultWslDistribution ||
            condaEnvField.text != s.defaultCondaEnv ||
            condaPathField.text != s.defaultCondaPath ||
            extraArgsField.text != s.defaultExtraArgs
    }

    override fun apply() {
        val s = SagemathSettings.getInstance().state
        s.interpreterPath = interpreterField.text.trim()
        s.defaultWslDistribution = (wslCombo.selectedItem as? String ?: "").trim()
        s.defaultCondaEnv = condaEnvField.text.trim()
        s.defaultCondaPath = condaPathField.text.trim()
        s.defaultExtraArgs = extraArgsField.text.trim()
    }

    override fun reset() {
        val s = SagemathSettings.getInstance().state
        interpreterField.text = s.interpreterPath
        wslCombo.selectedItem = s.defaultWslDistribution
        condaEnvField.text = s.defaultCondaEnv
        condaPathField.text = s.defaultCondaPath
        extraArgsField.text = s.defaultExtraArgs
    }
}
