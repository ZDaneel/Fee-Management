package com.usts.fee_front.utils;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

/**
 * @author zdaneel
 */
public class Utils {

    public static String getResponseMessage(int code) {
        String message = "";
        switch (code) {
            case ResponseCode.SUCCESS:
                message = "登录成功";
                break;
            case ResponseCode.EMPTY_RESPONSE:
                message = "响应体为空";
                break;
            case ResponseCode.SERVER_ERROR:
                message = "服务器错误";
                break;
            case ResponseCode.JSON_SERIALIZATION:
                message = "JSON序列化错误";
                break;
            case ResponseCode.EXIT_SUCCESS:
                message = "退出成功";
                break;
            case ResponseCode.REQUEST_FAILED:
                message = "请求发送失败";
                break;
            case ResponseCode.UNCHANGED_INFORMATION:
                message = "未修改信息";
                break;
            default:
        }
        return message;
    }
    public static void showMessage(Context context, Message message) {
        Toast.makeText(context, getResponseMessage(message.what), Toast.LENGTH_SHORT).show();
    }
}
