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
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.usts.fee_front.R;
import com.usts.fee_front.databinding.FragmentFeeAddBinding;
import com.usts.fee_front.databinding.FragmentFeeEditBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.GlideCacheUtil;
import com.usts.fee_front.utils.GlideEngine;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 账单编辑和删除功能
 *
 * @author zdaneel
 */
public class FeeEditFragment extends Fragment {
    public static final String TAG = "FeeEditFragment";
    private FragmentFeeEditBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Fee fee;
    private String feeImageName;
    private String noteImageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeEditBinding.inflate(inflater, container, false);
        int feeId = FeeEditFragmentArgs.fromBundle(getArguments()).getFeeId();
        int classId = FeeEditFragmentArgs.fromBundle(getArguments()).getClassId();
        updateData(feeId);
        handleConfirmButton(classId);
        /*
         * 处理添加开支和小票图片
         * - 回调接口处理调用相册/相机结果
         * - 使用EasyPhotos处理相册/相机
         */
        handleEditFeeImage();
        handleEditNoteImage();
        return binding.getRoot();
    }

    private void updateData(int feeId) {
        OkHttpUtils.get(NetworkConstants.QUERY_FEE_URL + feeId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                if (dataJson != null) {
                    fee = mapper.readValue(dataJson, Fee.class);
                    handler.post(() -> {
                        binding.editFeeName.setText(fee.getFname());
                        binding.editFeeMoney.setText(String.valueOf(fee.getMoney()));
                        binding.editFeeAcceptor.setText(fee.getAcceptor());
                        String feeImageUrl = fee.getImageUrl();
                        String noteImageUrl = fee.getNoteUrl();
                        if (feeImageUrl != null) {
                            Glide.with(requireContext())
                                    .load(NetworkConstants.GET_IMAGE_URL + feeImageUrl)
                                    .apply(GlideCacheUtil.getCacheStrategy())
                                    .into(binding.editFeeImage);
                        } else {
                            binding.editFeeImage.setImageResource(R.drawable.ic_image_camera);
                        }
                        if (noteImageUrl != null) {
                            Glide.with(requireContext())
                                    .load(NetworkConstants.GET_IMAGE_URL + noteImageUrl)
                                    .apply(GlideCacheUtil.getCacheStrategy())
                                    .into(binding.editNoteImage);
                        } else {
                            binding.editNoteImage.setImageResource(R.drawable.ic_image_camera);
                        }
                    });
                }
            }
        });
    }

    private void handleEditNoteImage() {
        SelectCallback noteImageCallback = new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                Photo noteImage = photos.get(0);
                File file = new File(noteImage.path);
                OkHttpUtils.uploadImage(NetworkConstants.UPLOAD_IMAGE, file, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) throws JsonProcessingException {
                        if (dataJson != null) {
                            noteImageName = mapper.readValue(dataJson, String.class);
                            String noteImagePath = NetworkConstants.GET_IMAGE_URL + noteImageName;
                            handler.post(() ->
                                    Glide.with(requireActivity())
                                            .load(noteImagePath)
                                            .apply(GlideCacheUtil.getCacheStrategy())
                                            .into(binding.editNoteImage));
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        };

        binding.editNoteImage.setOnClickListener(view -> {
            EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                    .setFileProviderAuthority("com.usts.fee_front.fileprovider")
                    .start(noteImageCallback);
        });
    }

    private void handleEditFeeImage() {
        SelectCallback feeImageCallback = new SelectCallback() {
            @Override
            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                Photo feeImage = photos.get(0);
                File file = new File(feeImage.path);
                OkHttpUtils.uploadImage(NetworkConstants.UPLOAD_IMAGE, file, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) throws JsonProcessingException {
                        if (dataJson != null) {
                            feeImageName = mapper.readValue(dataJson, String.class);
                            String feeImagePath = NetworkConstants.GET_IMAGE_URL + feeImageName;
                            handler.post(() ->
                                    Glide.with(requireActivity())
                                            .load(feeImagePath)
                                            .apply(GlideCacheUtil.getCacheStrategy())
                                            .into(binding.editFeeImage));
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        };

        binding.editFeeImage.setOnClickListener(view -> {
            EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                    .setFileProviderAuthority("com.usts.fee_front.fileprovider")
                    .start(feeImageCallback);
        });
    }

    /**
     * 处理确认按钮
     */
    private void handleConfirmButton(int classId) {
        binding.btnConfirm.setOnClickListener(view -> {
            String fname = binding.editFeeName.getText().toString();
            String money = binding.editFeeMoney.getText().toString();
            String acceptor = binding.editFeeAcceptor.getText().toString();
            fee.setFname(fname);
            fee.setMoney(Double.valueOf(money));
            fee.setAcceptor(acceptor);
            fee.setImageUrl(feeImageName);
            fee.setNoteUrl(noteImageName);
            Log.e(TAG, fee.toString());
            try {
                String feeJson = mapper.writeValueAsString(fee);
                OkHttpUtils.post(NetworkConstants.UPDATE_FEE_URL, feeJson, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) {
                        handler.post(() -> {
                            Toast.makeText(requireContext(), "修改成功!", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new FeeDetailFragmentArgs.Builder()
                                    .setFeeId(fee.getId())
                                    .setClassId(fee.getCollegeClassId())
                                    .build()
                                    .toBundle();
                            NavHostFragment.findNavController(FeeEditFragment.this)
                                    .navigate(R.id.action_feeEditFragment_to_feeDetailFragment, bundle);
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
