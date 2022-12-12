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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.usts.fee_front.R;
import com.usts.fee_front.adapter.CommentAdapter;
import com.usts.fee_front.databinding.FragmentCommentListBinding;
import com.usts.fee_front.pojo.Comment;
import com.usts.fee_front.utils.CommonConstants;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前fee对应的问题列表
 *
 * @author zdaneel
 */
public class CommentListFragment extends Fragment {
    public static final String TAG = "CommentListFragment";
    private FragmentCommentListBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private CommentAdapter commentAdapter;
    private RecyclerView commentRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentListBinding.inflate(inflater, container, false);
        commentRecyclerView = binding.commentList;
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int feeId = CommentListFragmentArgs.fromBundle(getArguments()).getFeeId();
        int classId = CommentListFragmentArgs.fromBundle(getArguments()).getClassId();
        updateData(feeId, classId);
        handleAddButton(feeId);
        return binding.getRoot();
    }

    /**
     * 处理添加按钮
     * 如果开支已经关闭，不显示添加按钮
     *
     * @param feeId 开支id
     */
    private void handleAddButton(int feeId) {
        FloatingActionButton btnFeeAdd = binding.btnCommentAdd;
        OkHttpUtils.get(NetworkConstants.QUERY_FEE_STATUS_URL + feeId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                if (dataJson != null) {
                    Integer isClosed = mapper.readValue(dataJson, Integer.class);
                    handler.post(() -> {
                        if (CommonConstants.CLOSED == isClosed) {
                            btnFeeAdd.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        btnFeeAdd.setOnClickListener(view -> {
            Bundle bundle = new CommentAddFragmentArgs.Builder()
                    .setFeeId(feeId)
                    .build()
                    .toBundle();
            NavHostFragment.findNavController(CommentListFragment.this)
                    .navigate(R.id.action_commentListFragment_to_commentAddFragment, bundle);

        });
    }

    /**
     * feeId查询对应的评论信息
     */
    private void updateData(int feeId, int classId) {
        OkHttpUtils.get(NetworkConstants.QUERY_OPEN_COMMENTS_URL + feeId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                List<Comment> commentList;
                if (dataJson == null) {
                    commentList = new ArrayList<>();
                } else {
                    commentList = mapper.readValue(dataJson, new TypeReference<List<Comment>>() {
                    });
                }
                handler.post(() -> {
                    commentAdapter = new CommentAdapter(commentList);
                    CommentAdapter.CallBack callBack = comment -> {
                        Bundle bundle = new CommentDetailFragmentArgs.Builder()
                                .setCommentId(comment.getId())
                                .setClassId(classId)
                                .build()
                                .toBundle();
                        NavHostFragment.findNavController(CommentListFragment.this)
                                .navigate(R.id.action_commentListFragment_to_commentDetailFragment, bundle);
                    };
                    commentAdapter.setCallBack(callBack);
                    commentRecyclerView.setAdapter(commentAdapter);
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
