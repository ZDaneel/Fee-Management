package com.usts.feeback.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.usts.feeback.utils.Constants.SESSION_STUDENT_DTO;

/**
 * @author leenadz
 * @since 2022-12-08 00:54
 */
@Api(tags = "学生信息接口")
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @ApiOperation(value = "根据id查询学生-测试用", hidden = true)
    @GetMapping("/query/{status}/{id}/{name}")
    public Result<Student> queryStudents(@PathVariable("id") Integer id,
                                         @PathVariable("status") Integer status,
                                         @PathVariable("name") Integer name,
                                         HttpServletRequest request) {
        Student student = studentService.getOne(
                Wrappers
                        .<Student>lambdaQuery()
                        .eq(Student::getId, id));
        HttpSession session = request.getSession();
        String id1 = session.getId();
        log.info("使用session后获取的sessionId: " + id1);
        return Result.success(student);
    }

    @ApiOperation(value = "登录操作")
    @PostMapping("/login")
    public Result<StudentDTO> login(@RequestBody Student student, HttpServletRequest request) {
        return studentService.login(student, request);
    }


    @ApiOperation(value = "查询当前用户权限信息", hidden = true)
    @GetMapping("/role/{classId}")
    public Result<Integer> queryRole(@PathVariable("classId") Integer classId) {
        return Result.success(studentService.queryRole(classId));
    }

    @ApiOperation(value = "登出操作")
    @GetMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_STUDENT_DTO);
        return Result.success();
    }

    @ApiOperation(value = "session查询当前用户信息")
    @GetMapping("/me")
    public Result<StudentDTO> queryStudent() {
        return Result.success(StudentHolder.getStudent());
    }
}
