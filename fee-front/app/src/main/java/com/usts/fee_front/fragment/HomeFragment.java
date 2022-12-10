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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usts.fee_front.R;
import com.usts.fee_front.adapter.CollegeClassAdapter;
import com.usts.fee_front.databinding.FragmentHomeBinding;
import com.usts.fee_front.pojo.CollegeClass;
import com.usts.fee_front.pojo.Student;
import com.usts.fee_front.utils.NetworkConstants;
import com.usts.fee_front.utils.OkHttpCallback;
import com.usts.fee_front.utils.OkHttpUtils;
import com.usts.fee_front.utils.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 * 展示用户加入的班级
 *
 * @author zdaneel
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private FragmentHomeBinding binding;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private CollegeClassAdapter collegeClassAdapter;
    private RecyclerView mCollegeClassRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mCollegeClassRecyclerView = binding.classList;
        mCollegeClassRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateData();
        return binding.getRoot();
    }

    /**
     * 请求数据并展示
     */
    private void updateData() {
        OkHttpUtils.get(NetworkConstants.QUERY_CLASSES_URL, new OkHttpCallback() {
            @Override
            public void onFinish(String dataJson) throws JsonProcessingException {
                List<CollegeClass> collegeClassList;
                if (dataJson == null) {
                    collegeClassList = new ArrayList<>();
                } else {
                    collegeClassList = mapper.readValue(dataJson, new TypeReference<List<CollegeClass>>() {
                    });
                }

                handler.post(() -> {
                    collegeClassAdapter = new CollegeClassAdapter(collegeClassList);
                    /*
                     * 回调接口处理班级被点击事件
                     */
                    CollegeClassAdapter.CallBack callBack = collegeClass -> {
                        /*
                         * 使用SafeArgs传递被点击的班级id
                         */
                        Bundle bundle = new FeeListFragmentArgs.Builder()
                                .setClassId(collegeClass.getId())
                                .build()
                                .toBundle();
                        NavHostFragment.findNavController(HomeFragment.this)
                                .navigate(R.id.action_navigation_home_to_feeListFragment, bundle);
                    };
                    collegeClassAdapter.setCallBack(callBack);
                    mCollegeClassRecyclerView.setAdapter(collegeClassAdapter);
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
