package com.usts.feeback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.CommentMapper;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.service.CollegeClassService;
import com.usts.feeback.service.CommentService;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.usts.feeback.utils.Constants.CLOSED;
import static com.usts.feeback.utils.Constants.COMMENT_KEY;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CollegeClassService collegeClassService;
    @Resource
    private FeeService feeService;
    @Resource
    private CommentMapper commentMapper;

    /**
     * 关闭评论线程池，并发量小，但不能失败，使用CallerRunsPolicy策略
     * 线程池信息：
     * - 核心线程数量：5
     * - 最大数量：5
     * - 超出核心线程数量的线程存活时间：30秒
     * - 队列大小：10
     * - 拒绝策略：CallerRunsPolicy
     */
    private static final ThreadPoolExecutor COMMENT_CLOSE_EXECUTOR = new ThreadPoolExecutor(
            5,
            5,
            30,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 异步评论关闭处理
     */
    private class CommentCloseHandler implements Runnable {
        private final Comment comment;
        public CommentCloseHandler(Comment comment) {
            this.comment = comment;
        }
        @Override
        public void run() {
            commentClose(comment);
        }
    }

    /**
     * 关闭评论
     *
     * @param comment 要关闭的评论
     */
    private void commentClose(Comment comment) {
        comment.setClosed(1);
        updateById(comment);
        String key =  COMMENT_KEY + comment.getId();
        stringRedisTemplate.delete(key);
    }

    @Override
    public List<Comment> queryOpenParentComments(Integer feeId) {
        /*
         * 查询未关闭的父级评论
         */
        return commentMapper.queryOpenParentComments(feeId);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void handleCommentListClose(List<Comment> closedCommentList) {
        for (Comment comment : closedCommentList) {
            if (CLOSED != comment.getClosed()) {
                CommentCloseHandler commentCloseHandler = new CommentCloseHandler(comment);
                COMMENT_CLOSE_EXECUTOR.submit(commentCloseHandler);
            }
        }
    }

    @Override
    public Result<Boolean> insertParentComment(Comment comment) {
        Integer studentId = StudentHolder.getStudent().getId();
        comment.setStudentId(studentId);
        save(comment);
        Integer commentId = comment.getId();
        if (commentId == null) {
            return Result.error("插入失败");
        }
        String key =  COMMENT_KEY + commentId;
        stringRedisTemplate.opsForSet().add(key, "0");
        return Result.success();
    }

    /**
     * 判断是否可以关闭
     *
     * @param comment 评论
     * @return false-不能 true-可以
     */
    private boolean judgeClosed(Comment comment) {

        /*
         * 判断是否有Fee可以关闭
         * 1. 获取确认的id集合
         *      - 如果不存在，说明已经过期，则直接关闭
         *      - 注意key查询不到时返回的[]，而不是null
         */
        Integer commentId = comment.getId();
        String key = COMMENT_KEY + commentId;
        Set<String> confirmSet = stringRedisTemplate.opsForSet().members(key);
        if (confirmSet == null) {
            return true;
        }
        if (confirmSet.size() == 0) {
            return true;
        }
        /*
         * 2. 判断发起质疑者是否在集合内
         */
        if (!confirmSet.contains(comment.getStudentId().toString())) {
            return false;
        }
        /*
         * 3. 查询对应的fee是否已经关闭
         */
        Integer feeId = comment.getTargetId();
        LambdaQueryWrapper<Fee> feeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        feeLambdaQueryWrapper
                .eq(Fee::getId, feeId)
                .select(Fee::getCollegeClassId, Fee::getClosed);
        Fee fee = feeService.getOne(feeLambdaQueryWrapper);
        Integer feeStatus = fee.getClosed();
        if (CLOSED == feeStatus) {
            return true;
        }
        /*
         * 4. 根据班级id查询人数
         *      - 确认人数超过总人数的一半，说明可以关闭
         *      - 集合中有一个假数据，需要-1才能得到真正确认的人数
         *      - 人数为1做特殊处理，否则会出现1>1的情况
         */
        Integer classId = fee.getCollegeClassId();
        Integer studentCount = collegeClassService.getStudentCount(classId);
        studentCount = studentCount / 2;
        int confirmCount = confirmSet.size() - 1;
        if (confirmCount == 1 && studentCount == 1) {
            return true;
        }
        return confirmCount > studentCount;
    }

    @Override
    public Result<Boolean> confirmComment(Integer commentId) {
        /*
         * 往Redis对应的key中加入当前用户的id
         */
        Integer studentId = StudentHolder.getStudent().getId();
        String key = COMMENT_KEY + commentId;
        stringRedisTemplate.opsForSet().add(key, studentId.toString());

        /*
         * 判断是否可以关闭，如果可以，直接关闭
         */
        Comment comment = getById(commentId);
        if (judgeClosed(comment)) {
            commentClose(comment);
        }
        return Result.success();
    }

    @SneakyThrows
    @Override
    public Comment queryParentComment(Integer commentId) {
        /*
         * 1. 查询commentId的评论
         * 2. 查询pid为commentId的评论
         * 3. 将子评论列表放入父评论的replyList中
         * 4. 将已经确认的ids放入confirmList
         */
        Comment parentComment = commentMapper.queryParentCommentByCommentId(commentId);
        List<Comment> childCommentList = commentMapper.queryChildCommentsByParentCommentId(commentId);
        parentComment.setReplyList(childCommentList);
        String key = COMMENT_KEY + commentId;
        Set<String> members = stringRedisTemplate.opsForSet().members(key);
        parentComment.setConfirmIds(members);
        return parentComment;
    }

    @Override
    public Result<Boolean> insertChildComment(Comment comment) {
        /*
         * 首先判断该comment是否被异步关闭
         */
        Comment parentComment = getById(comment.getPid());
        if (parentComment == null) {
            return Result.error("不存在父级评论!");
        }
        if (CLOSED == parentComment.getClosed()) {
            return Result.error("该问题已经被关闭!");
        }
        Integer studentId = StudentHolder.getStudent().getId();
        comment.setStudentId(studentId);
        save(comment);
        Integer commentId = comment.getId();
        if (commentId == null) {
            return Result.error("插入失败!");
        }
        return Result.success();
    }
}

