package com.zysy.tools.uitool;

import com.zysy.tools.FileUtils;
import com.intellij.openapi.ui.Messages;

import java.io.UnsupportedEncodingException;

public class PanelUtils {
    private static  String UI_DEFINE_PATH = "\\Common\\uidefine.lua";

    private static  String UI_PANEL_PATH = "\\View\\%s.lua";
    private static  String UI_PANEL_CTRL_PATH = "\\Controller\\%s.lua";
    private static  String UI_PANEL_ROOT_PATH = "\\View\\ViewRoot\\%sRoot.lua";


    public static boolean AddUIName(String uiName) {
        String defineFilePath = UIToolWindow.PROJECT_PATH + UI_DEFINE_PATH;
        String content = FileUtils.ReadFile(defineFilePath);

        if (content.contains(uiName)) {
            return false;
        }

        //����ĩβ
        int index = content.indexOf("}");

        StringBuilder new_content = new StringBuilder(content);
        new_content.insert(index,  String.format("\t%s = \'%s\',\r\n", uiName, uiName));

        FileUtils.WriteFile(defineFilePath, new_content.toString());

        return true;
    }

    public static boolean GeneratePanelRootLuaFile(String uiName) {
        String rootFilePath = UIToolWindow.PROJECT_PATH + String.format(UI_PANEL_ROOT_PATH, uiName);

        if (FileUtils.ExistFile(rootFilePath)) {
            return false;
        }

//        String template = "--[[{0}Root���ɹ���Lua/Lua Panel lpp]]\n" +
//                "{0}Root={}\n" +
//                "local nodes\n" +
//                "---@type PanelNodeGetter\n" +
//                "local nodeGetter\n" +
//                "local switch = require('switch')\n" +
//                "local nodeFuncSwitch = switch:new()\n" +
//                "---@param panelView {0}\n" +
//                "function {0}Root.InitRoot(panelView,transform)\n" +
//                "end\n" +
//                "function {0}Root.ReleaseRoot(panelView)\n" +
//                "end";
//
////        try {
////            template = new String(template.getBytes(), "UTF-8");
//            String content = template.replace("{0}", uiName);
//
//            FileUtils.NewFile(rootFilePath, content);
            return true;

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public static boolean GeneratePanelCtrlLuaFile(String uiName, String uiType) {
        String ctrlFilePath = UIToolWindow.PROJECT_PATH + String.format(UI_PANEL_CTRL_PATH, uiName);

        if (FileUtils.ExistFile(ctrlFilePath)) {
            return false;
        }

        String template = "--[[{0}Ctrl���ɹ������ɵ�]]\n" +
                "--������������ű�\n" +
                "require \"View/{0}\"\n" +
                "--�̳�BaseCtrl\n" +
                "{0}Ctrl = BaseCtrl:New()\n" +
                "\n" +
                "local this = {0}Ctrl\n" +
                "local view = {0}\n" +
                "local luaBehaviour\n" +
                "local transform\n" +
                "local gameObject\n" +
                "\n" +
                "function {0}Ctrl:New()\n" +
                "\tlocal tmp = {}\n" +
                "\tlog(\"{0}Ctrl New\")\n" +
                "\tsetmetatable(tmp, self)\n" +
                "\tself.__index = self\n" +
                "\treturn tmp\n" +
                "end\n" +
                "\n" +
                "--��д\n" +
                "function {0}Ctrl.GetUIName()\n" +
                "\treturn UIName.{0}\n" +
                "end\n" +
                "\n" +
                "function WelfareDailyDealPanelCtrl.GetUIType()\n" +
                "\treturn UIType.{1}\n" +
                "end\n" +
                "\n" +
                "--���洴��\n" +
                "function {0}Ctrl.OnCreate(go)\n" +
                "\tgameObject = go\n" +
                "\ttransform = go.transform\n" +
                "\tthis.__view = view\n" +
                "\tluaBehaviour = gameObject:GetComponent('LuaBehaviour')\n" +
                "\n" +
                "\t--TODO ���洴������߼����\n" +
                "\tlog('End lua Ctrl OnCreate-->', gameObject.name)\n" +
                "end\n" +
                "\n" +
                "--��������\n" +
                "function {0}Ctrl:OnMenuDestroy()\n" +
                "\tself:Reset()\n" +
                "\t--lua�����unityʵ�����ݣ���unityʵ�����ٺ�Ҫɾ�����ã�������ȫ�ͷš�\n" +
                "\tluaBehaviour = nil\n" +
                "\ttransform = nil\n" +
                "\tthis.__view = nil\n" +
                "\tgameObject = nil\n" +
                "\n" +
                "\t--TODO ����رպ���߼����\n" +
                "\tlog('destroy menu ',self.GetUIName())\n" +
                "end\n" +
                "\n" +
                "--������¼�\n" +
                "function {0}Ctrl.EnterMenuEvents()\n" +
                "\t--TODO�򿪽���Lua�߼�\n" +
                "end\n" +
                "\n" +
                "--����ر��¼�\n" +
                "function {0}Ctrl.ExitMenuEvents()\n" +
                "end\n" +
                "\n" +
                "--��д����\n" +
                "\n" +
                "--��Ҫһ��Ҫ���ڽű��Ľ�β \n" +
                "return {0}Ctrl";
//        try {
//            template = new String(template.getBytes(), "UTF-8");
            String content = template.replace("{0}", uiName);
            content = content.replace("{1}", uiType);

            FileUtils.NewFile(ctrlFilePath, content);
            return true;

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return false;
//        }
    }

    public static boolean GeneratePanelLuaFile(String uiName) {
        //root �ļ�·��
        String panelFilePath = UIToolWindow.PROJECT_PATH + String.format(UI_PANEL_PATH, uiName);

        //�Ѵ���
        if (FileUtils.ExistFile(panelFilePath)) {
            return false;
        }
        String template = "--[[{0}���ɹ�������]]\n" +
                "require \"View/ViewRoot/{0}Root\"\n" +
                "local transform;\n" +
                "local gameObject;\n" +
                "\n" +
                "{0} = {}\n" +
                "local this = {0}\n" +
                "\n" +
                "function {0}.OnInitUiRoot(obj)\n" +
                "\t\tgameObject = obj\n" +
                "\t\ttransform = obj.transform\n" +
                "\t\tlog(\"OnInitUiRoot lua -->> \",gameObject.name)\n" +
                "\n" +
                "\t\t{0}Root.InitRoot(this,transform)\n" +
                "end\n" +
                "\n" +
                "function {0}.OnDestroy()\n" +
                "\t\tlog(\"OnDestroy lua-- >> \",gameObject.name)\n" +
                "\t\tCtrlManager.OnDestroyMenu(UIName.{0})\n" +
                "\t\t--�뽫��unity�Ľӿ��л�ȡ������ɾ������֤�����ܹ��ͷ�\n" +
                "\t\ttransform=nil\n" +
                "\t\tgameObject = nil\n" +
                "\t\t{0}Root.ReleaseRoot(this)\n" +
                "end\n" +
                "\n";
//        try {
//            template = new String(template.getBytes(), "UTF-8");
            String content = template.replace("{0}", uiName);

            FileUtils.NewFile(panelFilePath, content);
            return true;

//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return false;
//        }
    }
}
