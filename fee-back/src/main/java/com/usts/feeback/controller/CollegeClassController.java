package com.usts.feeback.controller;

import com.usts.feeback.dto.CollegeClassDTO;
import com.usts.feeback.pojo.CollegeClass;
import com.usts.feeback.service.CollegeClassService;
import com.usts.feeback.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leenadz
 * @since 2022-12-10 00:12
 */
@Api(tags = "班级信息接口")
@RestController
@RequestMapping("/college-class")
public class CollegeClassController {

    @Resource
    private CollegeClassService collegeClassService;

    @ApiOperation(value = "查询当前用户所在的班级")
    @GetMapping("/classes")
    public Result<List<CollegeClassDTO>> queryClasses() {
        return Result.success(collegeClassService.queryClasses());
    }
}
