package com.usts.feeback.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.dao.FeeMapper;
import com.usts.feeback.service.CollegeClassService;
import com.usts.feeback.service.CommentService;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.usts.feeback.utils.Constants.FEE_KEY;
import static com.usts.feeback.utils.Constants.FEE_KEY_TTL;

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
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CommentService commentService;

    @Resource
    private CollegeClassService collegeClassService;

    @Override
    public List<Fee> queryOpenFees(Integer classId) {
        /*
         * 根据班级id和未关闭查询
         */
        List<Fee> feeList = list(
                new LambdaQueryWrapper<Fee>()
                        .eq(Fee::getCollegeClassId, classId)
                        .eq(Fee::getClosed, 0)
        );
        if (feeList.size() == 0) {
            return null;
        }
        List<Fee> openFeeList = feeList.stream()
                .filter(fee -> BooleanUtil.isFalse(judgeClosed(fee)))
                .collect(Collectors.toList());
        List<Fee> closeFeeList = feeList.stream()
                .filter(fee -> !openFeeList.contains(fee))
                .collect(Collectors.toList());
        openFeeList.forEach(System.out::println);
        System.out.println("=============================");
        closeFeeList.forEach(System.out::println);
        return null;
    }

    /**
     * 判断是否可以关闭
     *
     * @param fee 账单
     * @return false-不能 true-可以
     */
    private boolean judgeClosed(Fee fee) {
        /*
         * 判断是否有Fee可以关闭
         * 1. 获取确认的id集合
         *      - 如果不存在，说明已经过期，则直接关闭
         *      - 注意key查询不到时返回的[]，而不是null
         */
        Integer feeId = fee.getId();
        String key = FEE_KEY + feeId;
        Set<String> confirmSet = stringRedisTemplate.opsForSet().members(key);
        if (confirmSet == null) {
            return true;
        }
        if (confirmSet.size() == 0) {
            return true;
        }
        /*
         * 2. 根据班级id查询发起质疑的学生ids
         *      - 查询当前fee对应的且pid为0（不是回复）的comment
         *      - ids都在集合内，说明质疑者都已经确认
         */
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Comment::getTargetId, feeId)
                .eq(Comment::getPid, 0)
                .select(Comment::getUserId);
        List<Comment> commentList = commentService.list(lambdaQueryWrapper);
        List<Integer> commentUserIdList = commentList
                .stream()
                .map(Comment::getUserId)
                .collect(Collectors.toList());
        for (Integer commentUserId : commentUserIdList) {
            if (!confirmSet.contains(commentUserId.toString())) {
                return false;
            }
        }
        /*
         * 3. 根据班级id查询人数
         *      - 确认人数超过总人数的一半，说明可以关闭
         *      - 集合中有一个假数据，需要-1才能得到真正确认的人数
         *      - 人数为1做特殊处理，否则会出现1>1的情况
         */
        Integer studentCount = collegeClassService.getStudentCount(fee.getCollegeClassId());
        studentCount = studentCount / 2;
        int confirmCount = confirmSet.size() - 1;
        if (confirmCount == 1 && studentCount == 1) {
            return true;
        }
        return confirmCount > studentCount;
    }

    @Override
    public Result<Boolean> insertFee(Fee fee) {
        /*
         * 获取新增后的id
         * 使用该id存入Redis，默认加入 -1
         *      如果元素数为1，说明没有过期且没有人确认
         *      如果元素数为0，说明已经过期
         */
        //save(fee); TODO 取消注释
        Integer feeId = fee.getId();
        if (feeId == null) {
            return Result.error("新增fee失败");
        }
        String key = FEE_KEY + feeId;
        stringRedisTemplate.opsForSet().add(key, "-1");
        stringRedisTemplate.expire(key, FEE_KEY_TTL, TimeUnit.MINUTES);
        return Result.success();
    }

    @Override
    public Result<Boolean> confirmFee(Integer feeId) {
        /*
         * 往Redis对应的key中加入当前用户的id
         */
        Integer studentId = StudentHolder.getStudent().getId();
        String key = FEE_KEY + feeId;
        stringRedisTemplate.opsForSet().add(key, studentId.toString());
        return Result.success();
    }

    @Override
    public Result<Boolean> cancelFee(Integer feeId) {
        /*
         * 往Redis对应的key中移除当前用户的id
         */
        Integer studentId = StudentHolder.getStudent().getId();
        String key = FEE_KEY + feeId;
        stringRedisTemplate.opsForSet().remove(key, studentId.toString());
        return Result.success();
    }
}
