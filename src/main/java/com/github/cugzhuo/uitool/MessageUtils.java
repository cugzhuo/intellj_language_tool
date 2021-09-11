package com.github.cugzhuo.uitool;

import com.github.cugzhuo.FileUtils;
import com.intellij.openapi.ui.Messages;

public class MessageUtils {
    private static  String MESSAGE_DEFINE_PATH = "\\Message\\MessageName.lua";

    public static void AddMessageName(String messageName, String tip) {
        String content = FileUtils.ReadFile(UIToolWindow.PROJECT_PATH + MESSAGE_DEFINE_PATH);

        if (content.contains(messageName)) {
            UIToolWindow.ShowMessage(String.format("MessageName \"%s\" has already defined.", messageName));
            return;
        }

        int index = content.indexOf("}");

        int hash = Math.abs(messageName.hashCode());

        while (content.contains(" = " + hash + ",")) {
            hash++;
        }

        StringBuilder new_content = new StringBuilder(content);

        if (tip.length() == 0) {
            new_content.insert(index, String.format("\t%s = %d,\r\n", messageName, hash));
        }
        else  {
            new_content.insert(index, String.format("\t%s = %d,\t--%s\r\n", messageName, hash, tip));
        }

        FileUtils.WriteFile(MESSAGE_DEFINE_PATH, new_content.toString());
    }
}
