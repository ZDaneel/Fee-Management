package com.usts.fee_front.utils;

import static android.content.Context.MODE_PRIVATE;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.LoginActivity;
import com.usts.fee_front.MyApplication;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 监听服务端的响应，获取服务端的正确/报错信息
 *
 * @author zdaneel
 */
public class OkHttpCallback implements Callback {
    private final String TAG = "OkHttpCallback";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());
    public String url;
    public String result;

    /**
     * 请求成功
     *
     * @param call     call
     * @param response response
     * @throws IOException io异常
     */
    @Override
    public void onResponse(@NonNull Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            Log.d(TAG, "url:" + url);
            ResponseBody body = response.body();
            if (body != null) {
                /*
                 * 取出后端返回json里的code和message
                 *      SUCCESS: 进一步处理
                 *      NEED_LOGIN: 重定位到登录页面
                 *      ERROR: 如未登陆、密码错误等，直接用Toast进行信息展示
                 */
                result = body.string();
                Result<Object> res = mapper.readValue(result, new TypeReference<Result<Object>>() {
                });
                Integer code = res.getCode();
                String message = res.getMessage();
                Log.d(TAG, "请求成功:" + result);
                if (ResponseCode.SUCCESS == code) {
                    JsonNode jsonNode = mapper.readTree(result).get("data");
                    if (jsonNode == null) {
                        onFinish(null);
                    } else {
                        onFinish(jsonNode.toString());
                    }
                } else if (ResponseCode.NEED_LOGIN == code) {
                    handler.post(() -> {
                        Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT).show();
                        SharedPreferences share = MyApplication.getContext().getSharedPreferences("Session", MODE_PRIVATE);
                        if (share != null) {
                            SharedPreferences.Editor editor = share.edit();
                            editor.clear();
                            editor.apply();
                        }
                        MyApplication.setStudent(null);
                        Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getContext().startActivity(intent);
                    });
                } else {
                    handler.post(() -> Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT).show());
                }
            }
        }
    }

    /**
     * 请求失败
     *
     * @param call call
     * @param e    exception
     */
    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        Log.d(TAG, "请求失败:" + e.toString());
        Log.d(TAG, "url:" + url);
        handler.post(() -> Toast.makeText(MyApplication.getContext(), "请求失败:", Toast.LENGTH_SHORT).show());
    }

    /**
     * 结束处理，实现类进行重写
     * jsonNode为json中data的数据
     *
     * @param dataJson data的json信息
     * @throws JsonProcessingException Json处理异常
     */
    public void onFinish(String dataJson) throws JsonProcessingException {
    }

}

