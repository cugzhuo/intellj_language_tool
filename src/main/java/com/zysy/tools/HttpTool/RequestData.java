package com.zysy.tools.HttpTool;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.intellij.json.JsonUtil;
import com.intellij.json.psi.JsonObject;

public class RequestData {
    public String Action;
    public String Params;

    public RequestData(String action, String params) {
        Action = action;
        Params = params;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
