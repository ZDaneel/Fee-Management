package com.usts.fee_front.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @author zdaneel
 */
public class OkHttpCallback implements Callback {
    private final String TAG = "OkHttpCallback";

    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());
    public String url;

    /**
     * 接口调用成功
     * @param call call
     * @param response response
     * @throws IOException io异常
     */
    @Override
    public void onResponse(@NonNull Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
            Log.d(TAG,"url:"+url);
            ResponseBody body = response.body();
            if (body != null) {
                //成功时获取接口数据
                String result = body.string();
                @SuppressWarnings("all")
                Result res = mapper.readValue(result, Result.class);
                Integer code = res.getCode();
                String message = res.getMessage();
                Log.d(TAG,"请求成功:"+ result);
                if (ResponseCode.SUCCESS == code) {
                    onFinish(result);
                } else {
                    handler.post(() -> Toast.makeText(MyApplication.context, message, Toast.LENGTH_SHORT).show());
                }
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        Log.d(TAG,"请求失败:"+e.toString());
        Log.d(TAG,"url:" + url);
    }

    public void onFinish(String msg) throws JsonProcessingException {}

}

