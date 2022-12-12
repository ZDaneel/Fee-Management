package com.usts.fee_front.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usts.fee_front.R;
import com.usts.fee_front.pojo.Comment;

import java.util.List;

/**
 * @author zdaneel
 */
public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyHolder>{
    private final List<Comment> commentList;
    private CallBack callBack;

    public ReplyAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ReplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ReplyHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface CallBack {
        /**
         * 点击事件
         * 传递被点击的评论
         *
         * @param comment 评论
         */
        void onClick(Comment comment);
    }

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

    public class ReplyHolder extends RecyclerView.ViewHolder {
        private final TextView mStudentView;
        private final TextView mContentView;
        private Comment comment;

        public ReplyHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_reply, parent, false));
            mStudentView = itemView.findViewById(R.id.item_reply_student);
            mContentView = itemView.findViewById(R.id.item_reply_content);
            itemView.setOnClickListener(view -> {
                CallBack callBack = getCallBack();
                if (callBack != null) {
                    callBack.onClick(comment);
                }
            });
        }

        public void bind(Comment comment) {
            this.comment = comment;
            String studentName = comment.getStudentName() + ": "+ "@" + comment.getToStudentName() + " ";
            String content = comment.getContent();
            mStudentView.setText(studentName);
            mContentView.setText(content);
        }
    }
}
