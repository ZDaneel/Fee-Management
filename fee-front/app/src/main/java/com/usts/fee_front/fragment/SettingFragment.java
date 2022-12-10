package com.usts.fee_front.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.LoginActivity;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.databinding.FragmentSettingBinding;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

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
        Student student = MyApplication.getStudent();
        binding.settingStudentName.append(" " + student.getSname());
        binding.settingStudentNo.append(" " + student.getSno());

        /*
         * 设置退出登录
         */
        binding.settingLogout.setOnClickListener(view1 -> {
            /*
             * 向后端发送请求 删除session
             */
            OkHttpUtils.get(NetworkConstants.LOGOUT_URL, new OkHttpCallback() {
                @Override
                public void onFinish(String dataJson) {
                    handler.post(() -> {
                        /*
                         * 删除shareReference和全局Student变量
                         * 之后退出
                         */
                        SharedPreferences share = requireContext().getSharedPreferences("Session", MODE_PRIVATE);
                        if (share != null) {
                            SharedPreferences.Editor editor = share.edit();
                            editor.clear();
                            editor.apply();
                        }
                        MyApplication.setStudent(null);
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
