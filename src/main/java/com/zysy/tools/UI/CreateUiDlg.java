package com.zysy.tools.UI;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.zysy.tools.HttpTool.HttpTool;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;

public class CreateUiDlg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textUI;
    private JComboBox comboBoxType;

    private Project _project;

    public CreateUiDlg(String uiName, Project project) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        _project = project;

        textUI.setText(uiName);

        InitCombo();

        this.setLocationRelativeTo(null);
    }

    public void InitCombo() {
        comboBoxType.removeAllItems();
        for (UIType type : UIType.values()) {
            comboBoxType.addItem(type);
        }

        comboBoxType.setSelectedIndex(7);
    }

    private void onOK() {
        // add your code here
        ProgressManager.getInstance().run(
                new Task.Modal(_project, "Create UI", false){
                    @Override
                    public void run(@NotNull ProgressIndicator indicator) {
                        //gen code
                        indicator.setText("Create UI");
                        HttpTool.DoPost("CreateUIFile", textUI.getText() + "|" + comboBoxType.getSelectedItem().toString());
                    }
                }
        );
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
