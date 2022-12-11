package com.usts.fee_front.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usts.fee_front.R;
import com.usts.fee_front.pojo.CollegeClass;
import com.usts.fee_front.pojo.Comment;

import java.util.List;

/**
 * @author zdaneel
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{
    private final List<Comment> commentList;
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

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CommentHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public interface CallBack{
        /**
         * 点击事件
         * 传递被点击的评论
         * @param comment 评论
         */
        void onClick(Comment comment);
    }

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private final TextView mTitleView;
        private final TextView mStudentView;
        private Comment comment;
        public CommentHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_comment, parent, false));
            mTitleView = itemView.findViewById(R.id.item_comment_title);
            mStudentView = itemView.findViewById(R.id.item_comment_student);
            itemView.setOnClickListener(view -> {
                CallBack callBack = getCallBack();
                if (callBack != null) {
                    callBack.onClick(comment);
                }
            });
        }

        public void bind(Comment comment) {
            this.comment = comment;
            String title = " " + comment.getTitle();
            String studentName = " " + comment.getStudentName();
            mTitleView.append(title);
            mStudentView.append(studentName);
        }
    }
}
