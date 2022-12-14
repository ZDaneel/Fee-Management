package com.usts.fee_front.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.usts.fee_front.MyApplication;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * OkHttpUtils文件，用于获取URL上get或者post、downFile等方法的信息
 *
 * @author zdaneel
 */
public class OkHttpUtils {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void login(String url, String json, OkHttpCallback callback) {
        callback.url = url;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        CLIENT.newCall(request).enqueue(callback);
    }

    public static void get(String url, OkHttpCallback callback) {
        String sessionId = getSessionId();
        callback.url = url;
        //构建请求头
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", sessionId)
                .build();
        //接收服务端的响应
        CLIENT.newCall(request).enqueue(callback);

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void post(String url, String json, OkHttpCallback callback) {
        String sessionId = getSessionId();
        callback.url = url;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", sessionId)
                .post(body)
                .build();
        CLIENT.newCall(request).enqueue(callback);
    }

    public static void uploadImage(String url, File file, OkHttpCallback callback) {
        // 请求体
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getName(),
                        MultipartBody.create(MediaType.parse("multipart/form-data"), file)
                ).build();
        // Post 请求
        String sessionId = getSessionId();
        callback.url = url;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("cookie", sessionId)
                .post(body)
                .build();
        CLIENT.newCall(request).enqueue(callback);
    }


    private static String getSessionId() {
        Context context = MyApplication.getContext();
        SharedPreferences share = context.getSharedPreferences("Session", MODE_PRIVATE);
        return share.getString("sessionId", "null");
    }
}

