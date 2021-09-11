package com.github.cugzhuo.intelljlanguagetool;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class LanguageSearchAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        TestDialog.main();
    }
}
