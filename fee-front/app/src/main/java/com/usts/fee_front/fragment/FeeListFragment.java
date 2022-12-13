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
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.adapter.FeeAdapter;
import com.usts.fee_front.databinding.FragmentFeeListBinding;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.CommonConstants;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 班级对应的账单列表
 *
 * @author zdaneel
 */
public class FeeListFragment extends Fragment {
    public static final String TAG = "FeeListFragment";
    private FragmentFeeListBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private FeeAdapter feeAdapter;
    private RecyclerView feeRecyclerView;

    private int spinnerStatus = 0;
    private int classId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFeeListBinding.inflate(inflater, container, false);
        feeRecyclerView = binding.feeList;
        feeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feeRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL));
        classId = FeeListFragmentArgs.fromBundle(getArguments()).getClassId();
        handleSpinner();
        handleSearch();
        handleAddButton();
        updateData();
        return binding.getRoot();
    }

    private void handleSearch() {
        binding.feeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e(TAG, "搜索按钮提交: " + s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    /**
     * 处理下拉框选择
     */
    private void handleSpinner() {
        binding.feeSpinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (CommonConstants.CLOSED == i) {
                    spinnerStatus = 1;
                } else {
                    spinnerStatus = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * 处理添加按钮
     * - 根据权限显示是否显示
     * - 点击后跳转到新增页面
     */
    private void handleAddButton() {
        FloatingActionButton btnFeeAdd = binding.btnFeeAdd;

        OkHttpUtils.get(NetworkConstants.QUERY_ROLE_URL + classId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                Integer role = mapper.readValue(dataJson, Integer.class);
                if (CommonConstants.BOOKKEEPER == role) {
                    handler.post(() -> btnFeeAdd.setEnabled(true));
                }
            }
        });

        btnFeeAdd.setOnClickListener(view -> {
            Bundle bundle = new FeeAddFragmentArgs.Builder()
                    .setClassId(classId)
                    .build()
                    .toBundle();
            NavHostFragment.findNavController(FeeListFragment.this)
                    .navigate(R.id.action_feeListFragment_to_feeAddFragment, bundle);
        });
    }

    /**
     * 获取传递过来的班级id 查询fee列表并展示
     */
    private void updateData() {

        OkHttpUtils.get(NetworkConstants.QUERY_OPEN_FEES_URL + classId, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                List<Fee> feeList;
                if (dataJson == null) {
                    feeList = new ArrayList<>();
                } else {
                    feeList = mapper.readValue(dataJson, new TypeReference<List<Fee>>() {
                    });
                }
                handler.post(() -> {
                    feeAdapter = new FeeAdapter(feeList);
                    FeeAdapter.CallBack callBack = fee -> {
                        Bundle bundle = new FeeDetailFragmentArgs.Builder()
                                .setFeeId(fee.getId())
                                .setClassId(classId)
                                .build()
                                .toBundle();
                        NavHostFragment.findNavController(FeeListFragment.this)
                                .navigate(R.id.action_feeListFragment_to_feeDetailFragment, bundle);
                    };
                    feeAdapter.setCallBack(callBack);
                    feeRecyclerView.setAdapter(feeAdapter);
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
