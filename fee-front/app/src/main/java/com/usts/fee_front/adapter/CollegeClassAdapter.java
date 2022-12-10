package com.usts.fee_front.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.fragment.FeeListFragment;
import com.usts.fee_front.pojo.CollegeClass;

import java.util.List;

/**
 * @author zdaneel
 */
public class CollegeClassAdapter extends RecyclerView.Adapter<CollegeClassAdapter.CollegeClassHolder>{
    private final List<CollegeClass> collegeClassList;
    private CallBack callBack;

    /**
     * 定义 setCallBack() 方法
     * @param callBack 回调
     */
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public interface CallBack{
        /**
         * 点击事件
         * 传递被点击的班级
         * @param collegeClass 班级
         */
        void onClick(CollegeClass collegeClass);
    }

    public CollegeClassAdapter(List<CollegeClass> collegeClassList) {
        this.collegeClassList = collegeClassList;
    }

    @NonNull
    @Override
    public CollegeClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CollegeClassHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeClassHolder holder, int position) {
        /*
         * 得到具体的班级并展示
         */
        CollegeClass collegeClass = collegeClassList.get(position);
        holder.bind(collegeClass);
    }

    @Override
    public int getItemCount() {
        return collegeClassList.size();
    }

    public class CollegeClassHolder extends RecyclerView.ViewHolder {

        private final TextView mNameView;
        private final TextView mNumberView;
        private CollegeClass mCollegeClass;

        public CollegeClassHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_class, parent, false));
            mNameView = itemView.findViewById(R.id.item_class_name);
            mNumberView = itemView.findViewById(R.id.item_class_number);
            itemView.setOnClickListener(view -> {
                CallBack callBack = getCallBack();
                if (callBack != null) {
                    callBack.onClick(mCollegeClass);
                }
            });
        }

        public void bind(CollegeClass collegeClass) {
            mCollegeClass = collegeClass;
            String name = " " + mCollegeClass.getCname();
            String numberString = " " + mCollegeClass.getNumber();
            mNameView.append(name);
            mNumberView.append(numberString);
        }
    }
}
