package com.usts.fee_front.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.LoginActivity;
import com.usts.fee_front.databinding.FragmentSettingBinding;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;
import com.usts.fee_front.utils.Result;

import java.util.Objects;

/**
 * @author zdaneel
 */
public class SettingFragment extends Fragment {

    public static final String TAG = "SettingFragment";
    private FragmentSettingBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
         * 请求后端获取当前用户数据并展示
         */
        OkHttpUtils.get(NetworkConstants.QUERY_SELF_URL, new OkHttpCallback() {
            @Override
            public void onFinish(String msg) throws JsonProcessingException {
                super.onFinish(msg);
                Result<Student> res = mapper.readValue(msg, new TypeReference<Result<Student>>() {
                });
                Student student = res.getData();
                Log.e(TAG, student.toString());
                handler.post(() -> {
                    binding.settingStudentName.append(" " + student.getSname());
                    binding.settingStudentNo.append(" " + student.getSno());
                });
            }
        });

        /*
         * 设置退出登录
         */
        binding.settingLogout.setOnClickListener(view1 -> {
            /*
             * 向后端发送请求
             */
            OkHttpUtils.get(NetworkConstants.LOGOUT_URL, new OkHttpCallback() {
                @Override
                public void onFinish(String msg) throws JsonProcessingException {
                    super.onFinish(msg);
                    handler.post(() -> {
                        /*
                         * 删除session后直接退出
                         * 另外也可以删除shareReference
                         */
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    });
                }
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
