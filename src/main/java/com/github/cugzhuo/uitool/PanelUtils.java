package com.github.cugzhuo.uitool;

import com.github.cugzhuo.FileUtils;
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

        //插入末尾
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

        String template = "--[[{0}Root是由工具Lua/Lua Panel 节点编辑窗口 生成的！by lpp]]\n" +
                "{0}Root={}\n" +
                "local nodes\n" +
                "---@type PanelNodeGetter\n" +
                "local nodeGetter\n" +
                "local switch = require('switch')\n" +
                "local nodeFuncSwitch = switch:new()\n" +
                "---@param panelView {0}\n" +
                "function {0}Root.InitRoot(panelView,transform)\n" +
                "end\n" +
                "function {0}Root.ReleaseRoot(panelView)\n" +
                "end";

//        try {
//            template = new String(template.getBytes(), "UTF-8");
            String content = template.replace("{0}", uiName);

            FileUtils.NewFile(rootFilePath, content);
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

        String template = "--[[{0}Ctrl是由工具生成的]]\n" +
                "--关联界面组件脚本\n" +
                "require \"View/{0}\"\n" +
                "--继承BaseCtrl\n" +
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
                "--重写\n" +
                "function {0}Ctrl.GetUIName()\n" +
                "\treturn UIName.{0}\n" +
                "end\n" +
                "\n" +
                "function WelfareDailyDealPanelCtrl.GetUIType()\n" +
                "\treturn UIType.{1}\n" +
                "end\n" +
                "\n" +
                "--界面创建\n" +
                "function {0}Ctrl.OnCreate(go)\n" +
                "\tgameObject = go\n" +
                "\ttransform = go.transform\n" +
                "\tthis.__view = view\n" +
                "\tluaBehaviour = gameObject:GetComponent('LuaBehaviour')\n" +
                "\n" +
                "\t--TODO 界面创建完成逻辑入口\n" +
                "\tlog('End lua Ctrl OnCreate-->', gameObject.name)\n" +
                "end\n" +
                "\n" +
                "--界面销毁\n" +
                "function {0}Ctrl:OnMenuDestroy()\n" +
                "\tself:Reset()\n" +
                "\t--lua缓存的unity实例数据，在unity实例销毁后要删除引用，才能完全释放。\n" +
                "\tluaBehaviour = nil\n" +
                "\ttransform = nil\n" +
                "\tthis.__view = nil\n" +
                "\tgameObject = nil\n" +
                "\n" +
                "\t--TODO 界面关闭后的逻辑入口\n" +
                "\tlog('destroy menu ',self.GetUIName())\n" +
                "end\n" +
                "\n" +
                "--界面打开事件\n" +
                "function {0}Ctrl.EnterMenuEvents()\n" +
                "\t--TODO打开界面Lua逻辑\n" +
                "end\n" +
                "\n" +
                "--界面关闭事件\n" +
                "function {0}Ctrl.ExitMenuEvents()\n" +
                "end\n" +
                "\n" +
                "--重写结束\n" +
                "\n" +
                "--主要一定要放在脚本的结尾 \n" +
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
        //root 文件路径
        String panelFilePath = UIToolWindow.PROJECT_PATH + String.format(UI_PANEL_PATH, uiName);

        //已存在
        if (FileUtils.ExistFile(panelFilePath)) {
            return false;
        }
        String template = "--[[{0}是由工具生成]]\n" +
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
                "\t\t--请将从unity的接口中获取的引用删除，保证对象能够释放\n" +
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
