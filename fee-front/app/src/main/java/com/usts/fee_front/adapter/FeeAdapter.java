package com.usts.fee_front.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usts.fee_front.R;
import com.usts.fee_front.pojo.Fee;

import java.util.List;

/**
 * @author zdaneel
 */
public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.FeeHolder>{
    private final List<Fee> feeList;
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
         * @param fee 支出
         */
        void onClick(Fee fee);
    }

    public FeeAdapter(List<Fee> feeList) {
        this.feeList = feeList;
    }

    @NonNull
    @Override
    public FeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new FeeHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FeeHolder holder, int position) {
        holder.bind(feeList.get(position));
    }

    @Override
    public int getItemCount() {
        return feeList.size();
    }

    public class FeeHolder extends RecyclerView.ViewHolder {

        private final TextView mNameView;
        private final TextView mMoneyView;
        private final TextView mAcceptorView;
        private final TextView mDateView;
        private final ImageView mImageView;
        private Fee fee;

        public FeeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_fee, parent, false));
            // TODO 图片view处理
            mNameView = itemView.findViewById(R.id.item_fee_name);
            mMoneyView = itemView.findViewById(R.id.item_fee_money);
            mAcceptorView = itemView.findViewById(R.id.item_fee_acceptor);
            mDateView = itemView.findViewById(R.id.item_fee_date);
            mImageView = itemView.findViewById(R.id.item_fee_image);
            itemView.setOnClickListener(view -> {
                CallBack callBack = getCallBack();
                if (callBack != null) {
                    callBack.onClick(fee);
                }
            });
        }


        public void bind(Fee fee) {
            this.fee = fee;
            String fname = " " + fee.getFname();
            String money = " " + fee.getMoney();
            String acceptor = " " + fee.getAcceptor();
            // TODO 日期格式处理 图片处理
            mImageView.setImageResource(R.drawable.ic_image_camera);
            String date = " " + fee.getCreateTime();
            mNameView.append(fname);
            mMoneyView.append(money);
            mAcceptorView.append(acceptor);
            mDateView.append(date);
        }
    }
}
