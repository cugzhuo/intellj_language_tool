package com.github.cugzhuo;

import com.github.cugzhuo.uitool.UIToolWindow;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.tools.ant.taskdefs.Classloader;
import org.apache.xmlgraphics.util.ClasspathResource;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;

public class FileUtils {

    public static String ReadFile(String uri) {
        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(uri);

        try {
            String content = VfsUtilCore.loadText(virtualFile);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showMessageDialog("读取文件失败：" + uri,"警告", null);
            return null;
        }
    }

    public static void WriteFile(String uri, String content) {
        File file = new File(uri);

        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()),"utf-8");
            BufferedWriter bw = new BufferedWriter(write);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showMessageDialog("写入文件失败: " + uri,"警告", null);
        }
    }

    public static boolean ExistFile(String uri) {
        File file = new File(uri);
        return file.exists();
    }

    public static void NewFile(String uri, String content) {
        try {
            if (!FileUtils.ExistFile(uri)) {
                File file = new File(uri);
                file.createNewFile();
                LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            }

            WriteFile(uri, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
