package com.usts.feeback.controller;

import com.usts.feeback.dto.CollegeClassDTO;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "对应班级id中未关闭的支出列表")
    @GetMapping("/open-fees/{classId}")
    public Result<List<Fee>> queryOpenFees(@PathVariable("classId") Integer classId) {
        return Result.success(feeService.queryOpenFees(classId));
    }

    @ApiOperation(value = "对应班级id中已关闭的支出列表")
    @GetMapping("/closed-fees/{classId}")
    public Result<List<Fee>> queryClosedFees(@PathVariable("classId") Integer classId) {
        return Result.success();
    }

    @ApiOperation(value = "新增支出")
    @PostMapping("/fee")
    public Result<Boolean> insertFee(@RequestBody Fee fee) {
        return feeService.insertFee(fee);
    }

    @ApiOperation(value = "确认支出")
    @GetMapping("/confirm/{feeId}")
    public Result<Boolean> confirmFee(@PathVariable("feeId") Integer feeId) {
        return feeService.confirmFee(feeId);
    }

    @ApiOperation(value = "取消确认支出")
    @GetMapping("/cancel/{feeId}")
    public Result<Boolean> cancelFee(@PathVariable("feeId") Integer feeId) {
        return feeService.cancelFee(feeId);
    }

}
