package com.zysy.tools.Language;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.playback.commands.ActionCommand;
import com.zysy.tools.HttpTool.HttpTool;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Function;

public class LanguageDlg extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textContent;
    private JComboBox comboRemark;
    private JCheckBox checkBoxPublish;
    private JCheckBox checkBoxReplace;

    private Editor _editor = null;
    private Caret _caret = null;
    private ArrayList<String> allRemarks;

    public LanguageDlg() {
        InitDlg();
    }

    public LanguageDlg(String content, boolean editable, Editor editor, Caret caret) {
        _editor = editor;
        _caret = caret;

        InitDlg();

        checkBoxReplace.setEnabled(editor != null && caret != null);
        checkBoxReplace.setSelected(editor != null && caret != null);

        textContent.setText(content);
        textContent.setEditable(editable);
    }

    public void InitDlg() {
        setContentPane(contentPane);
        setModal(true);

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

//         call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        allRemarks = LanguageTool.GetAllRemark(_editor.getProject().getBasePath());
        SetupComboBox(allRemarks);

        this.setLocationRelativeTo(null);
        comboRemark.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { onComboBoxEdit(e); }
        });
    }

    private void SetupComboBox(ArrayList<String> list) {
        comboRemark.removeAllItems();
        for (int i = 0; i < list.size(); ++i) {
            comboRemark.addItem(list.get(i));
        }
    }

    private void onOK() {
        String content = textContent.getText();

        String remark = comboRemark.getEditor().getItem().toString();
        //request
        ProgressManager.getInstance().run(new Task.Modal(_editor.getProject(), "Executing", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setText("Request Execute Command");
                HttpTool.DoPost("AddLanguage", content + "|" + remark);
            }
        });

        //replace
        if (checkBoxReplace.isSelected()) {
            int newID = LanguageTool.FindLanguage(_editor.getProject().getBasePath(), content, remark);
            if (newID != -1) {
                String replaceStr = String.format("LanguageHelper.Get_Client_content(ELanguageID.E_%d)", newID);

                WriteCommandAction.runWriteCommandAction(_editor.getProject(), new Runnable() {
                    @Override
                    public void run() {
                        _editor.getDocument().replaceString(_caret.getSelectionStart(), _caret.getSelectionEnd(), replaceStr);
                    }
                });
            }
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onComboBoxEdit(KeyEvent e) {
        String remarkText = comboRemark.getEditor().getItem().toString();

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            return;
        }

        if (remarkText.equals("")) {
            return;
        }

        for (int i = 0; i < allRemarks.size(); ++i) {
            String comboStr = allRemarks.get(i);
            if (comboStr.startsWith(remarkText)) {
                JTextField textField = (JTextField) comboRemark.getEditor().getEditorComponent();
                textField.setText(comboStr);
                textField.setCaretPosition(comboStr.length());
                textField.moveCaretPosition(remarkText.length());
            }
        }
    }
}
