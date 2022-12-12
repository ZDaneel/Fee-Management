package com.usts.feeback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.utils.Result;

import java.util.List;

/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
public interface CommentService extends IService<Comment> {

    /**
     * 开支id所对应的简略版评论
     * @param feeId 开支id
     * @return 评论列表
     */
    List<Comment> queryOpenParentComments(Integer feeId);

    /**
     * 开启异步任务关闭任务
     * @param closedCommentList 需要关闭的评论列表
     */
    void handleCommentListClose(List<Comment> closedCommentList);

    /**
     * 新增父级评论
     * @param comment 评论
     * @return 是否成功
     */
    Result<Boolean> insertParentComment(Comment comment);

    /**
     * 确认评论
     * @param commentId 评论id
     * @return 结果
     */
    Result<Boolean> confirmComment(Integer commentId);

    /**
     * 通过评论id查询该id对应的信息和对应回复的信息
     * @param commentId 评论id
     * @return 评论信息
     */
    Comment queryParentComment(Integer commentId);

    /**
     * 新增子级评论
      * @param comment 新增的评论信息
     * @return 结果
     */
    Result<Boolean> insertChildComment(Comment comment);
}

