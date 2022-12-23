package com.usts.fee_front.fragment;


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
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.databinding.FragmentFeeDetailBinding;
import com.usts.fee_front.databinding.FragmentFeeListBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.CommonConstants;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.util.Date;

/**
 * 具体账单信息
 * 可以点击按钮查看质疑
 * @author zdaneel
 */
public class FeeDetailFragment extends Fragment {
    public static final String TAG = "FeeDetailFragment";
    public static final String ROLE_MESSAGE = "没有权限编辑";
    private FragmentFeeDetailBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeDetailBinding.inflate(inflater, container, false);
        int feeId = FeeDetailFragmentArgs.fromBundle(getArguments()).getFeeId();
        int classId = FeeDetailFragmentArgs.fromBundle(getArguments()).getClassId();
        updateData(feeId);
        checkButtonHandler(feeId, classId);
        editButtonHandler(feeId, classId);
        return binding.getRoot();
    }

    private void editButtonHandler(int feeId, int classId) {
        binding.btnEdit.setOnClickListener(view -> {
            OkHttpUtils.get(NetworkConstants.QUERY_ROLE_URL + classId, new OkHttpCallback() {
                @Override
                public void onFinish(String dataJson) throws JsonProcessingException {
                    Integer role = mapper.readValue(dataJson, Integer.class);
                    if (CommonConstants.BOOKKEEPER != role) {
                        handler.post(() -> Toast.makeText(MyApplication.getContext(), ROLE_MESSAGE, Toast.LENGTH_SHORT).show());
                    } else {
                        handler.post(() -> {
                            Bundle bundle = new FeeEditFragmentArgs.Builder()
                                    .setFeeId(feeId)
                                    .setClassId(classId)
                                    .build()
                                    .toBundle();
                            NavHostFragment.findNavController(FeeDetailFragment.this)
                                    .navigate(R.id.action_feeDetailFragment_to_feeEditFragment, bundle);
                        });
                    }
                }
            });
        });
    }

    private void checkButtonHandler(int feeId, int classId) {
        binding.btnCheck.setOnClickListener(view -> {
            Bundle bundle = new CommentListFragmentArgs.Builder()
                    .setFeeId(feeId)
                    .setClassId(classId)
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
                        String feeImageUrl = fee.getImageUrl();
                        String noteImageUrl = fee.getNoteUrl();
                        if (feeImageUrl != null) {
                            Glide.with(requireContext())
                                    .load(NetworkConstants.GET_IMAGE_URL + feeImageUrl)
                                    .into(binding.detailFeeImage);
                        } else {
                            binding.detailFeeImage.setImageResource(R.drawable.ic_image_camera);
                        }
                        if (noteImageUrl != null) {
                            Glide.with(requireContext())
                                    .load(NetworkConstants.GET_IMAGE_URL + noteImageUrl)
                                    .into(binding.detailNoteImage);
                        } else {
                            binding.detailNoteImage.setImageResource(R.drawable.ic_image_camera);
                        }
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
