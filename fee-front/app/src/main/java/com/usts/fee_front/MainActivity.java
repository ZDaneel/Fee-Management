package com.usts.fee_front;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.databinding.ActivityMainBinding;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;
import com.usts.fee_front.utils.ResponseCode;
import com.usts.fee_front.utils.Result;
import com.usts.fee_front.utils.Utils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * @author zdaneel
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Message message = new Message();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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
    }


    public void login(View view) throws JsonProcessingException {
        String sno = binding.studentNo.getText().toString();
        String password = binding.password.getText().toString();
        Student student = new Student();
        student.setSno(sno);
        student.setPassword(password);
        String studentJson = mapper.writeValueAsString(student);
        Log.e(TAG, studentJson);
        OkHttpUtils.login(NetworkConstants.LOGIN_URL, studentJson, new OkHttpCallback() {
            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                super.onResponse(call, response);
                // 处理session
                Headers headers =response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                String sessionId = session.substring(0,session.indexOf(";"));
                Log.e(TAG, "从后端获得的: " + sessionId);
                // 存入文件
                SharedPreferences share = getSharedPreferences("Session", MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                edit.putString("sessionId",sessionId);
                edit.apply();
            }

            @Override
            public void onFinish(int status, String msg) throws JsonProcessingException {
                super.onFinish(status, msg);
                if (status == ResponseCode.SUCCESS) {
                    handler.post(() -> {
                        // 清空数据
                        binding.studentNo.setText("");
                        binding.password.setText("");
                        // 处理页面的跳转
                        test(view);
                    });
                }
            }
        });
    }

    public void test(View view) {
        OkHttpUtils.get(NetworkConstants.BASE_URL + "/student/query/1",
                new OkHttpCallback() {
                    @Override
                    public void onFinish(int status, String msg) throws JsonProcessingException {
                        if (status == ResponseCode.SUCCESS) {
                            Result<Student> result = mapper.readValue(msg, new TypeReference<Result<Student>>() {});
                            Student student = result.getData();
                            handler.post(()->{
                                binding.studentNo.setText(student.toString());
                            });
                        } else {
                            message.what = ResponseCode.REQUEST_FAILED;
                            handler.post(()-> Utils.showMessage(getApplicationContext(), message));
                        }
                    }
                });
    }
}