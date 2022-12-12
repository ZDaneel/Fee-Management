package com.usts.fee_front.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.R;
import com.usts.fee_front.databinding.FragmentFeeDetailBinding;
import com.usts.fee_front.databinding.FragmentFeeListBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.util.Date;

/**
 * 具体账单信息
 * 可以点击按钮查看质疑与确认
 * @author zdaneel
 */
public class FeeDetailFragment extends Fragment {
    public static final String TAG = "FeeDetailFragment";
    private FragmentFeeDetailBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeDetailBinding.inflate(inflater, container, false);
        int feeId = FeeDetailFragmentArgs.fromBundle(getArguments()).getFeeId();
        updateData(feeId);
        checkButtonHandler(feeId);
        return binding.getRoot();
    }

    private void checkButtonHandler(int feeId) {
        binding.btnCheck.setOnClickListener(view -> {
            Bundle bundle = new CommentListFragmentArgs.Builder()
                    .setFeeId(feeId)
                    .build()
                    .toBundle();
            NavHostFragment.findNavController(FeeDetailFragment.this)
                    .navigate(R.id.action_feeDetailFragment_to_commentListFragment, bundle);
        });
    }

    /**
     * feeId查询详细信息
     */
    private void updateData(int feeId) {
        OkHttpUtils.get(NetworkConstants.QUERY_FEE_URL + feeId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                if (dataJson != null) {
                    Fee fee = mapper.readValue(dataJson, Fee.class);
                    handler.post(() -> {
                        String fname = " " + fee.getFname();
                        String moneyString = " " + fee.getMoney();
                        String acceptor = " " + fee.getAcceptor();
                        binding.detailFeeName.append(fname);
                        binding.detailFeeMoney.append(moneyString);
                        binding.detailFeeAcceptor.append(acceptor);
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
