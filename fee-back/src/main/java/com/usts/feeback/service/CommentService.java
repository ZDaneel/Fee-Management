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
     * 取消评论
     * @param commentId 评论id
     * @return 结果
     */
    Result<Boolean> cancelComment(Integer commentId);
}

