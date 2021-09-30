package com.zysy.tools.Actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.zysy.tools.HttpTool.HttpTool;
import com.zysy.tools.Language.LanguageDlg;
import com.zysy.tools.Language.LanguageTool;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddLanguageAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Caret caret = editor.getCaretModel().getCurrentCaret();
        String content = caret.getSelectedText();

        //save
        FileDocumentManager.getInstance().saveDocument(editor.getDocument());

        StringBuilder sb = new StringBuilder(content);
        // if start with " or ' then remove it
        if (content.startsWith("\"") || content.startsWith("\'")) {
            sb.delete(0, 1);
        }
        // if end with " or ' then remove it
        if (content.endsWith("\"") || content.endsWith("\'")) {
            sb.delete(content.length() - 2, content.length() - 1);
        }

        //dlg
        LanguageDlg dialog = new LanguageDlg(sb.toString(), false, editor, caret);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Caret caret = editor.getCaretModel().getCurrentCaret();
        e.getPresentation().setEnabledAndVisible(caret != null && caret.getSelectedText() != null);
    }
}
