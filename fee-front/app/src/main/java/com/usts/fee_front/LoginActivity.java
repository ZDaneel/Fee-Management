package com.usts.fee_front;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.databinding.ActivityLoginBinding;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.InputTextMsgDialog;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;
import com.usts.fee_front.utils.ResponseCode;
import com.usts.fee_front.utils.Result;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author zdaneel
 */
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // 设置登陆按钮点击监听
        binding.login.setOnClickListener(view1 -> {
            try {
                login(view);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        binding.test.setOnClickListener(view1 -> {
            test(view);
        });

        setImage();
    }

    private void setImage() {
        OkHttpUtils.get(NetworkConstants.QUERY_LOGIN_IMAGE_URL, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                Log.e(TAG, "dataJson: " + dataJson);
                String fileName = mapper.readValue(dataJson, String.class);
                String imageUrl = NetworkConstants.GET_IMAGE_URL + fileName;
                handler.post(() ->
                        Glide.with(LoginActivity.this).load(imageUrl).into(binding.loginImage));
            }
        });
    }


    /**
     * 登陆处理
     *
     * @param view view
     * @throws JsonProcessingException json异常处理
     */
    public void login(View view) throws JsonProcessingException {
        /*
         * 1. 取出输出的信息(判空交给后端)
         * 2. 封装Student类并转换成json
         */
        String sno = binding.studentNo.getText().toString();
        String password = binding.password.getText().toString();
        Student student = new Student();
        student.setSno(sno);
        student.setPassword(password);
        /*
         * 使用OkHttp进行异步通信
         * 对onResponse和onFinish进行重写
         */
        String studentJson = mapper.writeValueAsString(student);
        OkHttpUtils.login(NetworkConstants.LOGIN_URL, studentJson, new OkHttpCallback() {

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "请求失败");
                    Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                }
                ResponseBody body = response.body();
                if (body != null) {
                    String string = body.string();
                    Result<Student> res = mapper.readValue(string, new TypeReference<Result<Student>>() {
                    });
                    Integer code = res.getCode();
                    String message = res.getMessage();
                    Student data = res.getData();
                    if (ResponseCode.SUCCESS == code) {
                        /*
                         * 将学生数据存储到Application中
                         * 登陆成功，获取并设置session到本地
                         */
                        MyApplication.setStudent(data);
                        Headers headers = response.headers();
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        String sessionId = session.substring(0, session.indexOf(";"));
                        Log.e(TAG, "从后端获得的: " + sessionId);
                        SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
                        SharedPreferences.Editor edit = share.edit();
                        edit.putString("sessionId", sessionId);
                        edit.apply();
                        onFinish(null);
                    } else {
                        handler.post(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
                    }
                }
            }

            @Override
            public void onFinish(String dataJson) {
                handler.post(() -> {
                    binding.studentNo.setText("");
                    binding.password.setText("");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    public void test(View view) {
        OkHttpUtils.get(NetworkConstants.BASE_URL + "/student/query/0/1?name=john",
                new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) throws JsonProcessingException {
                        Student student = mapper.readValue(dataJson, Student.class);
                        handler.post(() -> binding.studentNo.setText(student.toString()));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
