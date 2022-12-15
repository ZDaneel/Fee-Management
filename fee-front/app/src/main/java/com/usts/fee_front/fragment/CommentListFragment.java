package com.usts.fee_front.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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

    private int spinnerStatus = 0;
    private String title;
    private int classId;
    private int feeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentListBinding.inflate(inflater, container, false);
        commentRecyclerView = binding.commentList;
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        feeId = CommentListFragmentArgs.fromBundle(getArguments()).getFeeId();
        classId = CommentListFragmentArgs.fromBundle(getArguments()).getClassId();
        handleSpinner();
        handleSearch();
        handleAddButton();
        updateData(null);
        return binding.getRoot();
    }

    private void handleSearch() {
        binding.commentSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e(TAG, "改变搜索" + s);
                title = s;
                updateData(s);
                return false;
            }
        });
    }

    /**
     * 处理下拉框选择
     */
    private void handleSpinner() {
        binding.commentSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerStatus == i) {
                    return;
                }
                spinnerStatus = (1 == i) ? 1 : 0;
                updateData(title);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 处理添加按钮
     * 如果开支已经关闭，不显示添加按钮
     *
     */
    private void handleAddButton() {
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
    private void updateData(String title) {
        String url;
        String baseUrl;
        if (CommonConstants.CLOSED == spinnerStatus) {
            baseUrl = NetworkConstants.QUERY_CLOSED_COMMENTS_URL + feeId;
        } else {
            baseUrl = NetworkConstants.QUERY_OPEN_COMMENTS_URL + feeId;
        }
        if (title == null) {
            url = baseUrl;
        } else {
            url = baseUrl + "/" + title;
        }
        OkHttpUtils.get(url, new OkHttpCallback() {
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
