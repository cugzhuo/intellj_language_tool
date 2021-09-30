package com.zysy.tools.uitool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.wm.ToolWindow;
import com.zysy.tools.HttpTool.HttpTool;
import com.zysy.tools.UI.UIType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;


public class UIToolWindow {
    private JTextField uiname_text_field;
    private JButton generate_btn;
    private JPanel main_view;
    private JCheckBox create_class_check;
    private JComboBox ui_type_combo;
    private JTextField project_path_text;
    private JTextField message_text_field;
    private JComboBox message_group_combo;
    private JButton btn_new_message;
    private JTextField message_tip_text_field;
    private JPanel webViewPanel;

    private Project curProject;
    public static String PROJECT_PATH;

    public UIToolWindow(ToolWindow tool_window) {
        generate_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OnBtnNewUiDown();
            }
        });

        btn_new_message.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OnBtnNewMessageDown();
            }
        });

        InitProjectPath();
        InitCombo();
    }

    public void InitCombo() {
        ui_type_combo.removeAllItems();
        for (UIType type : UIType.values()) {
            ui_type_combo.addItem(type);
        }

        ui_type_combo.setSelectedIndex(7);
    }

    public JPanel GetContent() {
        return  main_view;
    }

    public void InitProjectPath() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();

        if (projects.length > 0) {
            curProject = projects[0];
            VirtualFile[] vFiles = ProjectRootManager.getInstance(curProject).getContentSourceRoots();

            if (vFiles.length > 0) {
                PROJECT_PATH = vFiles[0].getPresentableUrl();
                project_path_text.setText(PROJECT_PATH);
            }
        }
    }

    private void OnBtnNewUiDown() {
        String uiName = uiname_text_field.getText();

        if (uiName.isEmpty()) {
            UIToolWindow.ShowMessage("������UI Name");
            return;
        }

        if (!uiName.contains("Panel")) {
            UIToolWindow.ShowMessage("UI Name");
            return;
        }

        boolean nameSuccess = PanelUtils.AddUIName(uiName);

        String tips = "";
        if (nameSuccess) {
            tips += "UI Name: " + uiName + "";
        }

        boolean rootSuccess = false;
        boolean ctrlSuccess = false;
        boolean panelSuccess = false;

        String uiType = ui_type_combo.getSelectedItem().toString();
        if (create_class_check.isSelected()) {
            rootSuccess = PanelUtils.GeneratePanelRootLuaFile(uiName);
            ctrlSuccess = PanelUtils.GeneratePanelCtrlLuaFile(uiName, uiType);
            panelSuccess = PanelUtils.GeneratePanelLuaFile(uiName);

            if (rootSuccess) {
                tips += ("\n" + uiName + "Root.lua" + "");
            }
            if (ctrlSuccess) {
                tips += ("\n" + uiName + "Ctrl.lua" + "");
            }
            if (panelSuccess) {
                tips += ("\n" + uiName + ".lua" + "");
            }
            UIToolWindow.ShowMessage(tips);
        }
        else if (!nameSuccess) {
            UIToolWindow.ShowMessage("");
        }

    }

    private void OnBtnNewMessageDown() {
//        String message = message_text_field.getText();
//
//        if (message.isEmpty()) {
//            UIToolWindow.ShowMessage("������Message Name");
//            return;
//        }
//
//        String tip = message_text_field.getText();
//
//        MessageUtils.AddMessageName(message, tip);
    }

    public static void ShowMessage(String content) {
        try {
//            content = new String(content.getBytes(), "UTF-8");
            String title = "";
            title = new String(title.getBytes(), "UTF-8");

            Messages.showMessageDialog(content, title, null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
