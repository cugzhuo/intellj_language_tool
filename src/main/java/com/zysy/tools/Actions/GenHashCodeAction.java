package com.zysy.tools.Actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.vfs.*;
import com.zysy.tools.HttpTool.HttpTool;
import org.jetbrains.annotations.NotNull;

import java.rmi.activation.ActivationGroupDesc;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class GenHashCodeAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        //save
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        FileDocumentManager.getInstance().saveDocument(editor.getDocument());


        Project project = e.getData(CommonDataKeys.PROJECT);
        ProgressManager.getInstance().run(
                new Task.Modal(project, "Gen HashCode", false){
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        //gen code
                        indicator.setText("Gen Hash Code");
                        HttpTool.DoPost("MessageToLua",  "");
                        //refresh
                        indicator.setText("Refresh File");
                        file.refresh(true, true);
                    }
                }
        );
    }

    @Override
    public void update(AnActionEvent event) {
        VirtualFile file = CommonDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String fileName = file.getName();
        event.getPresentation().setEnabledAndVisible(fileName.contains("MessageName"));
    }
}
