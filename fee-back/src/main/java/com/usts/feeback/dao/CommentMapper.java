package com.usts.feeback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usts.feeback.pojo.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据feeId查询评论列表
     * @param feeId 开支id
     * @return 评论列表
     */
    List<Comment> queryOpenParentComments(@Param("feeId") Integer feeId);
}

