package com.usts.fee_front.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.databinding.FragmentFeeAddBinding;
import com.usts.fee_front.databinding.FragmentFeeListBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.GlideEngine;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 账单添加功能
 *
 * @author zdaneel
 */
public class FeeAddFragment extends Fragment {
    public static final String TAG = "FeeAddFragment";
    private FragmentFeeAddBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private String feeImageName;
    private String noteImageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeAddBinding.inflate(inflater, container, false);
        int classId = FeeListFragmentArgs.fromBundle(getArguments()).getClassId();
        handleConfirmButton(classId);
        /*
         * 处理添加开支和小票图片
         * - 回调接口处理调用相册/相机结果
         * - 使用EasyPhotos处理相册/相机
         */
        handleAddFeeImage();
        handleAddNoteImage();
        return binding.getRoot();
    }

    private void handleAddNoteImage() {
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
                            handler.post(() -> {
                                Glide.with(requireActivity())
                                        .load(noteImagePath)
                                        .into(binding.addNoteImage);
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        };

        binding.addNoteCamera.setOnClickListener(view -> {
            EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                    .setFileProviderAuthority("com.usts.fee_front.fileprovider")
                    .start(noteImageCallback);
        });
    }

    private void handleAddFeeImage() {
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
                            handler.post(() -> {
                                Glide.with(requireActivity())
                                        .load(feeImagePath)
                                        .into(binding.addFeeImage);
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        };

        binding.addFeeCamera.setOnClickListener(view -> {
            EasyPhotos.createAlbum(this, true, false, GlideEngine.getInstance())
                    .setFileProviderAuthority("com.usts.fee_front.fileprovider")
                    .start(feeImageCallback);
        });
    }

    /**
     * 处理确认按钮
     *
     * @param classId 班级id
     */
    private void handleConfirmButton(int classId) {
        binding.btnConfirm.setOnClickListener(view -> {
            // TODO 检测未填写
            Fee fee = new Fee();
            String fname = binding.addFeeName.getText().toString();
            String money = binding.addFeeMoney.getText().toString();
            String acceptor = binding.addFeeAcceptor.getText().toString();
            fee.setClosed(0);
            fee.setFname(fname);
            fee.setMoney(Double.valueOf(money));
            fee.setAcceptor(acceptor);
            fee.setCollegeClassId(classId);
            fee.setImageUrl(feeImageName);
            fee.setNoteUrl(noteImageName);
            Log.e(TAG, fee.toString());
            try {
                String feeJson = mapper.writeValueAsString(fee);
                OkHttpUtils.post(NetworkConstants.INSERT_FEE_URL, feeJson, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) {
                        handler.post(() -> {
                            Glide.with(requireActivity()).clear(binding.addFeeImage);
                            Glide.with(requireActivity()).clear(binding.addNoteImage);
                            binding.addFeeName.setText("");
                            binding.addFeeMoney.setText("");
                            binding.addFeeAcceptor.setText("");
                            Toast.makeText(requireContext(), "添加成功!", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new FeeListFragmentArgs.Builder()
                                    .setClassId(fee.getCollegeClassId())
                                    .build()
                                    .toBundle();
                            NavHostFragment.findNavController(FeeAddFragment.this)
                                    .navigate(R.id.action_feeAddFragment_to_feeListFragment, bundle);
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
