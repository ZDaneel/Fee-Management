package com.usts.feeback.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.dao.FeeMapper;
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
        return feeList
                .stream()
                .filter(fee -> BooleanUtil.isFalse(judgeClosed(fee)))
                .collect(Collectors.toList());
    }

    /**
     * 判断是否可以关闭
     * @param fee 账单
     * @return false-不能 true-可以
     */
    private boolean judgeClosed(Fee fee) {
        /*
         * 判断是否有Fee可以关闭
         *      1. 获取确认的id集合
         *      2. 根据班级id查询发起质疑的学生ids
         *          判断是否符合close要求-ids都在集合内
         *      3. 根据班级id查询人数
         *          判断是否符合close要求-确认人数超过总人数的一半
         */
        String key = FEE_KEY + fee.getId();
        Set<String> confirmSet = stringRedisTemplate.opsForSet().members(key);

        return false;
    }

    @Override
    public Result<Boolean> insertFee(Fee fee) {
        /*
         * 获取新增后的id
         * 使用该id存入Redis
         */
        //save(fee); TODO 取消注释
        Integer feeId = fee.getId();
        //Integer studentId = StudentHolder.getStudent().getId(); TODO 取消注释
        if (feeId == null) {
            return Result.error("新增fee失败");
        }
        String key = FEE_KEY + feeId;
        // TODO 修改为当前id
        stringRedisTemplate.opsForSet().add(key, "1");
        stringRedisTemplate.expire(key, FEE_KEY_TTL, TimeUnit.MINUTES);
        return Result.success();
    }

    @Override
    public Result<Boolean> confirmFee(Integer feeId) {
        /*
         * 往Redis对应的key中加入当前用户的id
         */
        //Integer studentId = StudentHolder.getStudent().getId(); TODO 取消注释
        Integer studentId = 4;
        String key = FEE_KEY + feeId;
        stringRedisTemplate.opsForSet().add(key, studentId.toString());
        return Result.success();
    }

    @Override
    public Result<Boolean> cancelFee(Integer feeId) {
        /*
         * 往Redis对应的key中移除当前用户的id
         */
        //Integer studentId = StudentHolder.getStudent().getId(); TODO 取消注释
        Integer studentId = 0;
        String key = FEE_KEY + feeId;
        stringRedisTemplate.opsForSet().remove(key, studentId.toString());
        return Result.success();
    }
}
