package com.zysy.tools.Language;

import com.zysy.tools.uitool.UIToolWindow;
import kotlin.reflect.jvm.internal.impl.protobuf.Internal;

import java.io.*;
import java.util.ArrayList;

public class LanguageTool {
    private static ArrayList< String[] > ReadLanguageTable(String projectPath) {
//        String projectPath = UIToolWindow.PROJECT_PATH;
        String tablePath = projectPath + "\\..\\..\\DataConfigBuild\\DataConfigs\\Languages\\chinese\\0$Client.csv";

        ArrayList<String[]> tableLines = new ArrayList<String[]>();
        String line = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tablePath), "UTF-8"))) {
            while ((line = br.readLine()) != null) {
                tableLines.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tableLines;
    }

    public static ArrayList<String> GetAllRemark(String projectPath) {
        ArrayList<String> allRemarks = new ArrayList<String>();
        ArrayList<String[]> allLines = ReadLanguageTable(projectPath);

        for (int i = 2; i < allLines.size(); ++i) {
            String remark = allLines.get(i)[0];
            if (!remark.equals("")) {
                allRemarks.add(remark);
            }
        }

        return allRemarks;
    }

    public static int FindLanguage(String projectPath, String content, String remark) {
        ArrayList<String[]> allLines = ReadLanguageTable(projectPath);

        for (int i = 2; i < allLines.size(); ++i) {
            String[] line = allLines.get(i);
            if (line.length < 3) {
                continue;
            }
            String str1 = line[2];
            String str2 = line[1];
            if (str1.equals(content) && str2.equals(remark)) {
                return Integer.parseInt(line[1]);
            }
        }

        return -1;
    }
}
