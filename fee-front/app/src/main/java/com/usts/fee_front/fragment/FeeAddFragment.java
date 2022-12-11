package com.usts.fee_front.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.databinding.FragmentFeeAddBinding;
import com.usts.fee_front.databinding.FragmentFeeListBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

/**
 * 账单添加功能
 * @author zdaneel
 */
public class FeeAddFragment extends Fragment {
    public static final String TAG = "FeeAddFragment";
    private FragmentFeeAddBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeAddBinding.inflate(inflater, container, false);
        int classId = FeeListFragmentArgs.fromBundle(getArguments()).getClassId();
        handleConfirmButton(classId);
        binding.btnClear.setOnClickListener(view -> {
            // TODO 清空图片
            binding.addFeeName.setText("");
            binding.addFeeMoney.setText("");
            binding.addFeeAcceptor.setText("");
        });
        return binding.getRoot();
    }

    /**
     * 处理确认按钮
     * @param classId 班级id
     */
    private void handleConfirmButton(int classId) {
        binding.btnConfirm.setOnClickListener(view -> {
            // TODO 检测未填写 图片处理
            Fee fee = new Fee();
            String fname = binding.addFeeName.getText().toString();
            String money = binding.addFeeMoney.getText().toString();
            String acceptor = binding.addFeeAcceptor.getText().toString();
            fee.setClosed(0);
            fee.setFname(fname);
            fee.setMoney(Double.valueOf(money));
            fee.setAcceptor(acceptor);
            fee.setCollegeClassId(classId);
            try {
                String feeJson = mapper.writeValueAsString(fee);
                OkHttpUtils.post(NetworkConstants.INSERT_PARENT_COMMENT_URL, feeJson, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) {
                        handler.post(() -> {
                            // TODO 清空图片
                            binding.addFeeName.setText("");
                            binding.addFeeMoney.setText("");
                            binding.addFeeAcceptor.setText("");
                            Toast.makeText(requireContext(), "添加成功!", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
