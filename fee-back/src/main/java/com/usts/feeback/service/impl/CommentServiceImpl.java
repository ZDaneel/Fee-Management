package com.usts.feeback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.CommentMapper;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
@Service()
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

