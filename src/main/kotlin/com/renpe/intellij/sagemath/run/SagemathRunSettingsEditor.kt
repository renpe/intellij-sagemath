package com.renpe.intellij.sagemath.run

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class SagemathRunSettingsEditor(private val project: Project) : SettingsEditor<SagemathRunConfiguration>() {

    private val scriptField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleFile()
                .withTitle("SageMath Script")
                .withDescription("Select the .sage / .py / .spyx file to run"),
        )
    }
    private val scriptArgsField = JBTextField()
    private val workingDirField = TextFieldWithBrowseButton().apply {
        addBrowseFolderListener(
            project,
            FileChooserDescriptorFactory.singleDir()
                .withTitle("Working Directory")
                .withDescription("Leave blank to use the script's directory"),
        )
    }
    private val pythonModeBox = JBCheckBox("Run with --python (skip Sage preparser)")
    private val preparseOnlyBox = JBCheckBox("Preparse only (--preparse), don't execute")
    private val extraArgsField = JBTextField()

    override fun createEditor(): JComponent = panel {
        row("Script:") {
            cell(scriptField).align(AlignX.FILL)
                .comment("Path to the SageMath script to run.")
        }
        row("Script arguments:") {
            cell(scriptArgsField).align(AlignX.FILL)
                .comment("Passed to the script as <code>sys.argv</code> after the script path.")
        }
        row("Working directory:") {
            cell(workingDirField).align(AlignX.FILL)
                .comment("Empty = use the script's directory.")
        }
        row("") {
            cell(pythonModeBox)
                .comment("Useful for plain <code>.py</code> scripts that don't need preparsing.")
        }
        row("") {
            cell(preparseOnlyBox)
                .comment("Writes <code>SCRIPT.sage.py</code> next to the source and exits.")
        }
        row("Extra arguments:") {
            cell(extraArgsField).align(AlignX.FILL)
                .comment("Appended to the sage command line, after the global extra arguments.")
        }
    }

    override fun resetEditorFrom(s: SagemathRunConfiguration) {
        scriptField.text = s.scriptPath
        scriptArgsField.text = s.scriptArgs
        workingDirField.text = s.workingDirectory
        pythonModeBox.isSelected = s.pythonMode
        preparseOnlyBox.isSelected = s.preparseOnly
        extraArgsField.text = s.extraArgs
    }

    override fun applyEditorTo(s: SagemathRunConfiguration) {
        s.scriptPath = scriptField.text.trim()
        s.scriptArgs = scriptArgsField.text.trim()
        s.workingDirectory = workingDirField.text.trim()
        s.pythonMode = pythonModeBox.isSelected
        s.preparseOnly = preparseOnlyBox.isSelected
        s.extraArgs = extraArgsField.text.trim()
    }
}
