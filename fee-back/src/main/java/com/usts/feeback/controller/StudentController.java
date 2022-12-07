package com.usts.feeback.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author leenadz
 * @since 2022-12-08 00:54
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping("/query/{id}")
    public Result<Student> queryStudents(@PathVariable("id")Integer id) {
        Student student = studentService.getOne(
                Wrappers
                        .<Student>lambdaQuery()
                        .eq(Student::getId, id));
        return Result.success(student);
    }
}
