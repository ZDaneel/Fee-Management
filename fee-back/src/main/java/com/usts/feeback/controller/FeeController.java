package com.usts.feeback.controller;

import com.usts.feeback.dto.CollegeClassDTO;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leenadz
 * @since 2022-12-10 14:20
 */
@Api(tags = "班费支出信息")
@RestController
@RequestMapping("/fee")
public class FeeController {
    @Resource
    private FeeService feeService;

    @ApiOperation(value = "对应班级id中未关闭的支出列表, 查询参数为名称")
    @GetMapping(value = {"/open-fees/{classId}/{name}", "/open-fees/{classId}"})
    public Result<List<Fee>> queryOpenFees(@PathVariable("classId") Integer classId,
                                           @PathVariable(value = "name", required = false) String name) {
        return Result.success(feeService.queryOpenFees(classId, name));
    }

    @ApiOperation(value = "对应班级id中已关闭的支出列表, 查询参数为名称")
    @GetMapping(value = {"/closed-fees/{classId}/{name}", "/closed-fees/{classId}"})
    public Result<List<Fee>> queryClosedFees(@PathVariable("classId") Integer classId,
                                             @PathVariable(value = "name", required = false) String name) {
        System.out.println("classId: " + classId);
        System.out.println("name: " + name);
        return Result.success(feeService.queryClosedFees(classId, name));
    }

    @ApiOperation(value = "新增支出")
    @PostMapping("/fee")
    public Result<Boolean> insertFee(@RequestBody Fee fee) {
        return feeService.insertFee(fee);
    }

    @ApiOperation(value = "根据id查询fee详情")
    @GetMapping("/fee/{feeId}")
    public Result<Fee> queryFee(@PathVariable("feeId") Integer feeId) {
        return Result.success(feeService.getById(feeId));
    }

    @ApiOperation(value = "查询支出状态 是否关闭")
    @GetMapping("/status/{feeId}")
    public Result<Integer> queryFeeStatus(@PathVariable("feeId") Integer feeId) {
        return feeService.queryFeeStatus(feeId);
    }
}
