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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.adapter.ReplyAdapter;
import com.usts.fee_front.databinding.FragmentCommentDetailBinding;
import com.usts.fee_front.pojo.Comment;
import com.usts.fee_front.utils.CommonConstants;
import com.usts.fee_front.utils.InputTextMsgDialog;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 问题具体功能
 * 类似于评论区
 *
 * @author zdaneel
 */
public class CommentDetailFragment extends Fragment {
    public static final String TAG = "CommentDetailFragment";
    public static final String CLOSE_MESSAGE = "该主题已关闭";
    public static final String ROLE_MESSAGE = "没有权限回复";
    private FragmentCommentDetailBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private ReplyAdapter replyAdapter;
    private RecyclerView commentRecyclerView;
    private Comment parentComment;
    private Integer classId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentDetailBinding.inflate(inflater, container, false);
        commentRecyclerView = binding.commentDetailList;
        int commentId = CommentDetailFragmentArgs.fromBundle(getArguments()).getCommentId();
        classId = CommentDetailFragmentArgs.fromBundle(getArguments()).getClassId();
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateData(commentId);
        handleConfirmButton();
        handleParentCommentClick();
        return binding.getRoot();
    }

    /**
     * 确认按钮
     */
    private void handleConfirmButton() {
        binding.btnConfirm.setOnClickListener(view -> {
            OkHttpUtils.get(NetworkConstants.CONFIRM_COMMENT_URL + parentComment.getId(), new OkHttpCallback() {
                @Override
                public void onFinish(String dataJson) {
                    handler.post(() -> {
                        updateData(parentComment.getId());
                        binding.btnConfirm.setEnabled(false);
                        binding.btnConfirm.setText("已确认");
                    });
                }
            });
        });
    }

    /**
     * 内容点击后处理
     */
    private void handleParentCommentClick() {
        binding.parentCommentContent.setOnClickListener(view -> {
            if (CommonConstants.CLOSED == parentComment.getClosed()) {
                Toast.makeText(MyApplication.getContext(), CLOSE_MESSAGE, Toast.LENGTH_SHORT).show();
            } else {
                judgeRole(parentComment);
            }
        });
    }

    /**
     * 判断权限
     *
     * @param comment 当前评论
     */
    private void judgeRole(Comment comment) {
        OkHttpUtils.get(NetworkConstants.QUERY_ROLE_URL + classId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                Integer role = mapper.readValue(dataJson, Integer.class);
                List<Integer> roleList = Arrays.asList(CommonConstants.BOOKKEEPER, CommonConstants.CLASS_COMMITTEE);
                if (roleList.contains(role)) {
                    handler.post(() -> insertChildComment(comment));
                } else {
                    handler.post(() -> Toast.makeText(MyApplication.getContext(), ROLE_MESSAGE, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void updateData(Integer commentId) {
        OkHttpUtils.get(NetworkConstants.QUERY_COMMENT_URL + commentId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                parentComment = mapper.readValue(dataJson, Comment.class);
                List<Comment> childCommentList = parentComment.getReplyList();
                Set<String> confirmIds = parentComment.getConfirmIds();
                Integer studentId = MyApplication.getStudent().getId();
                handler.post(() -> {
                    if (confirmIds.contains(studentId.toString())) {
                        binding.btnConfirm.setEnabled(false);
                        binding.btnConfirm.setText("已确认");
                    }
                    String author = parentComment.getStudentName() + ": ";
                    binding.parentCommentTitle.setText(parentComment.getTitle());
                    binding.parentCommentContent.setText(parentComment.getContent());
                    binding.parentCommentStudent.setText(author);
                    replyAdapter = new ReplyAdapter(childCommentList);
                    ReplyAdapter.CallBack callBack = clickedComment -> {
                        if (CommonConstants.CLOSED == parentComment.getClosed()) {
                            Toast.makeText(MyApplication.getContext(), CLOSE_MESSAGE, Toast.LENGTH_SHORT).show();
                        } else {
                            judgeRole(clickedComment);
                        }
                    };
                    replyAdapter.setCallBack(callBack);
                    commentRecyclerView.setAdapter(replyAdapter);
                });

            }
        });
    }

    /**
     * 弹出弹窗并添加新的回复
     *
     * @param clickedComment 被点击的评论
     */
    private void insertChildComment(Comment clickedComment) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(requireActivity(), R.style.dialog_center);
        inputTextMsgDialog.show();
        inputTextMsgDialog.setmOnTextSendListener(msg -> {
            Comment childComment = new Comment();
            childComment.setContent(msg);
            childComment.setTargetId(parentComment.getTargetId());
            childComment.setPid(parentComment.getId());
            childComment.setToStudentId(clickedComment.getStudentId());
            childComment.setClosed(0);
            try {
                String childCommentJson = mapper.writeValueAsString(childComment);
                OkHttpUtils.post(NetworkConstants.INSERT_CHILD_COMMENT_URL, childCommentJson, new OkHttpCallback() {
                    @Override
                    public void onFinish(String dataJson) {
                        handler.post(() -> {
                            updateData(parentComment.getId());
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
