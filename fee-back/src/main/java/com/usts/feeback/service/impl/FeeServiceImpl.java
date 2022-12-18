package com.usts.feeback.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.dao.FeeMapper;
import com.usts.feeback.service.CommentService;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.usts.feeback.utils.Constants.BOOKKEEPER;
import static com.usts.feeback.utils.Constants.FEE_TTL;

/**
 * (Fee)表服务实现类
 *
 * @author makejava
 * @since 2022-12-10 13:11:02
 */
@Service
@Slf4j
public class FeeServiceImpl extends ServiceImpl<FeeMapper, Fee> implements FeeService {

    @Resource
    @Lazy
    private CommentService commentService;

    @Resource
    private StudentService studentService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<Fee> queryOpenFees(Integer classId, String name) {
        /*
         * 根据班级id和未关闭查询
         */
        List<Fee> feeList = list(
                new LambdaQueryWrapper<Fee>()
                        .eq(Fee::getCollegeClassId, classId)
                        .eq(Fee::getClosed, 0)
                        .like(null != name, Fee::getFname, name)
        );
        if (feeList.size() == 0) {
            return null;
        }
        /*
         * 过滤出超过一星期的支出并关闭
         */
        List<Fee> openFeeList = feeList.stream()
                .filter(fee -> !isTimeout(fee.getCreateTime()))
                .collect(Collectors.toList());
        List<Fee> timeoutFeeList = feeList.stream()
                .filter(fee -> !openFeeList.contains(fee))
                .collect(Collectors.toList());
        for (Fee fee : timeoutFeeList) {
            fee.setClosed(1);
            updateById(fee);
            /*
             * 异步关闭该fee下面所有的评论
             */
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLambdaQueryWrapper.eq(Comment::getTargetId, fee.getId());
            List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
            commentService.handleCommentListClose(commentList);
        }
        return openFeeList;
    }

    @Override
    public Result<Boolean> insertFee(Fee fee) {
        /*
         * 获取新增后的id
         */
        save(fee);
        System.out.println(fee.toString());
        Integer feeId = fee.getId();
        if (feeId == null) {
            return Result.error("新增fee失败");
        }
        return Result.success();
    }

    @Override
    public Result<Integer> queryFeeStatus(Integer feeId) {
        if (feeId == -1) {
            return Result.error("参数错误!");
        }
        LambdaQueryWrapper<Fee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Fee::getId, feeId).select(Fee::getClosed);
        Fee one = getOne(lambdaQueryWrapper);
        return Result.success(one.getClosed());
    }

    @Override
    public List<Fee> queryClosedFees(Integer classId, String name) {
        /*
         * 根据班级id和未关闭查询
         */
        List<Fee> feeList = list(
                new LambdaQueryWrapper<Fee>()
                        .eq(Fee::getCollegeClassId, classId)
                        .eq(Fee::getClosed, 1)
                        .like(null != name, Fee::getFname, name)
        );
        if (feeList.size() == 0) {
            return null;
        }
        return feeList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Boolean> deleteFee(Fee fee) {
        /*
         * 首先判断是否有权限删除
         */
        Integer role = studentService.queryRole(fee.getCollegeClassId());
        if (BOOKKEEPER != role) {
            return Result.error("你没有权限删除!");
        }
        /*
         * 先删除对应评论再删除当前支出
         */
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getTargetId, fee.getId()).select(Comment::getId);
        List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
        List<Integer> commentIds = commentList.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        boolean removeComments = commentService.removeByIds(commentIds);
        if (!removeComments) {
            return Result.error("删除失败");
        }
        return Result.success(removeById(fee.getId()));
    }

    /**
     * 是否已经超过讨论期
     * @param createTime 创建时间
     * @return true-超过 false-没超时
     */
    private boolean isTimeout(Date createTime) {
        Date date = new Date();
        long between = DateUtil.between(date, createTime, DateUnit.DAY);
        return between > FEE_TTL;
    }
}
