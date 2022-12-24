package com.usts.fee_front.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.usts.fee_front.databinding.FragmentCommentAddBinding;
import com.usts.fee_front.pojo.Comment;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

/**
 * 问题添加功能
 *
 * @author zdaneel
 */
public class CommentAddFragment extends Fragment {
    public static final String TAG = "CommentAddFragment";
    private FragmentCommentAddBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentAddBinding.inflate(inflater, container, false);
        Integer feeId = CommentAddFragmentArgs.fromBundle(getArguments()).getFeeId();
        Integer classId = CommentAddFragmentArgs.fromBundle(getArguments()).getClassId();
        handleConfirmButton(feeId, classId);
        return binding.getRoot();
    }

    /**
     * 确认添加
     *
     * @param feeId 开支id
     */
    private void handleConfirmButton(Integer feeId, Integer classId) {
        binding.addCommentButton.setOnClickListener(view -> {
            // TODO 检测为空
            String title = binding.addCommentTitle.getText().toString();
            String content = binding.addCommentContent.getText().toString();
            Comment comment = new Comment();
            comment.setPid(0);
            comment.setClosed(0);
            comment.setTargetId(feeId);
            comment.setTitle(title);
            comment.setContent(content);
            try {
                String commentJson = mapper.writeValueAsString(comment);
                OkHttpUtils.post(NetworkConstants.INSERT_PARENT_COMMENT_URL, commentJson, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) {
                        handler.post(() -> {
                            binding.addCommentTitle.setText("");
                            binding.addCommentContent.setText("");
                            Bundle bundle = new CommentListFragmentArgs.Builder()
                                    .setFeeId(comment.getTargetId())
                                    .setClassId(classId)
                                    .build()
                                    .toBundle();
                            NavHostFragment.findNavController(CommentAddFragment.this)
                                    .navigate(R.id.action_commentAddFragment_to_commentListFragment, bundle);
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
