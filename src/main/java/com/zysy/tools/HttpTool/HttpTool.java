package com.zysy.tools.HttpTool;

import com.jediterm.terminal.emulator.charset.CharacterSet;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class HttpTool {
    public static String DoGet(String action, String param) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();

            String url = String.format("http://127.0.0.1:7777/?Action=%s&Parames=%s", action, param);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);

            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            httpGet.addHeader("Content-Type", "application/json; charset=utf-8");

            CloseableHttpResponse response = client.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity, "UTF-8");

            response.close();

            return str;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String DoPost(String action, String arg) {
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build();

        RequestData data = new RequestData(action, arg);

        StringEntity entity = new StringEntity(data.toString(), Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        HttpPost httpPost = new HttpPost("http://127.0.0.1:7777/");
        httpPost.setHeader("ContentType", "application/json; charset=utf-8");
        httpPost.setConfig(requestConfig);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            String str = EntityUtils.toString(httpEntity, "UTF-8");

            response.close();

            return str;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}