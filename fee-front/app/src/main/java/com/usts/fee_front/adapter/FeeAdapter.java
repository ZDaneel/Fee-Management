package com.usts.fee_front.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.usts.fee_front.MyApplication;
import com.usts.fee_front.R;
import com.usts.fee_front.pojo.Fee;
import com.usts.fee_front.utils.GlideCacheUtil;
import com.usts.fee_front.utils.NetworkConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author zdaneel
 */
public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.FeeHolder> {
    private final List<Fee> feeList;
    private CallBack callBack;

    /**
     * 定义 setCallBack() 方法
     *
     * @param callBack 回调
     */
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public interface CallBack {
        /**
         * 点击事件
         * 传递被点击的班级
         *
         * @param fee 支出
         */
        void onClick(Fee fee);

        /**
         * 删除开支
         *
         * @param fee 开支
         */
        void delete(Fee fee);
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
        private final ImageButton mImageButton;
        private Fee fee;

        public FeeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_fee, parent, false));
            mNameView = itemView.findViewById(R.id.item_fee_name);
            mMoneyView = itemView.findViewById(R.id.item_fee_money);
            mAcceptorView = itemView.findViewById(R.id.item_fee_acceptor);
            mDateView = itemView.findViewById(R.id.item_fee_date);
            mImageView = itemView.findViewById(R.id.item_fee_image);
            mImageButton = itemView.findViewById(R.id.item_fee_delete);
            CallBack callBack = getCallBack();
            if (callBack != null) {
                itemView.setOnClickListener(view -> callBack.onClick(fee));
                mImageButton.setOnClickListener(view -> callBack.delete(fee));
            }
        }


        public void bind(Fee fee) {
            this.fee = fee;
            String fname = " " + fee.getFname();
            String money = " " + fee.getMoney();
            String acceptor = " " + fee.getAcceptor();
            Date createTime = fee.getCreateTime();
            String imageUrl = fee.getImageUrl();

            mNameView.append(fname);
            mMoneyView.append(money);
            mAcceptorView.append(acceptor);
            if (imageUrl != null) {
                Glide.with(MyApplication.getContext())
                        .load(NetworkConstants.GET_IMAGE_URL + imageUrl)
                        .apply(GlideCacheUtil.getCacheStrategy())
                        .into(mImageView);
            } else {
                Glide.with(MyApplication.getContext())
                        .load(R.drawable.ic_image_camera)
                        .apply(GlideCacheUtil.getCacheStrategy())
                        .into(mImageView);
            }
            // 使用java8的时间api转格式
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Instant instant = createTime.toInstant();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                String format = " " + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                mDateView.append(format);
            }
        }
    }
}
