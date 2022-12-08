package com.usts.fee_front.utils;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final Message message = new Message();
    public String url;
    public String result;

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
                result = body.string();
                Log.d(TAG,"请求成功:"+ result.toString());
                //mapper.readValue()
                //调用onFinish输出获取的信息，可用通过重写onFinish()方法，运用hashmap获取需要的值并存储
                onFinish(ResponseCode.SUCCESS, result);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call call, IOException e) {
        Log.d(TAG,"url:"+url);
        Log.d(TAG,"请求失败:"+e.toString());
        //请求失败，输出失败的原因
        try {
            onFinish(ResponseCode.REQUEST_FAILED,e.toString());
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    public void onFinish(int status,String msg) throws JsonProcessingException {}

}

