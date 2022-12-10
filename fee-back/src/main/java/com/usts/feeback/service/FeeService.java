package com.usts.feeback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.utils.Result;

import java.util.List;

/**
 * (Fee)表服务接口
 *
 * @author makejava
 * @since 2022-12-10 13:11:02
 */
public interface FeeService extends IService<Fee> {

    /**
     * 通过班级id查询未关闭的支出列表
     * @param classId 班级id
     * @return 支出列表
     */
    List<Fee> queryOpenFees(Integer classId);

    /**
     * 插入支出
     * @param fee 支出
     * @return 处理结果
     */
    Result<Boolean> insertFee(Fee fee);

    /**
     * 确认支出
     * @param feeId 支出id
     * @return 处理结果
     */
    Result<Boolean> confirmFee(Integer feeId);

    /**
     * 取消支出
     * @param feeId 支出id
     * @return 处理结果
     */
    Result<Boolean> cancelFee(Integer feeId);
}
