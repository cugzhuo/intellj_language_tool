package com.zysy.tools.Actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.zysy.tools.HttpTool.HttpTool;
import com.zysy.tools.UI.CreateUiDlg;
import org.jetbrains.annotations.NotNull;

public class CreatePanelAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Caret caret = editor.getCaretModel().getCurrentCaret();
        String content = caret.getSelectedText();

        //save
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        FileDocumentManager.getInstance().saveDocument(editor.getDocument());

        CreateUiDlg dialog = new CreateUiDlg(content, editor.getProject());
        dialog.pack();
        dialog.setVisible(true);

        //refresh
        file.refresh(true, true);
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Caret caret = editor.getCaretModel().getCurrentCaret();

        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String fileName = file.getName();

        e.getPresentation().setEnabledAndVisible(caret != null && caret.getSelectedText() != null && fileName.contains("uidefine"));
    }
}
