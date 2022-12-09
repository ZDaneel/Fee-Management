package com.usts.feeback.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @GetMapping("/query/{id}")
    public Result<Student> queryStudents(@PathVariable("id")Integer id, HttpServletRequest request) {
        Student student = studentService.getOne(
                Wrappers
                        .<Student>lambdaQuery()
                        .eq(Student::getId, id));
        HttpSession session = request.getSession();
        String id1 = session.getId();
        log.info("使用session后获取的sessionId: " + id1);
        return Result.success(student);
    }

    @PostMapping("/login")
    public Result<Boolean> login(@RequestBody Student student, HttpServletRequest request) {
        return studentService.login(student, request);
    }

    /**
     * 在拦截器中取出查询的用户，直接返回DTO数据
     * @return Result封装的StudentDTO数据
     */
    @GetMapping("/query_self")
    public Result<StudentDTO> querySelf() {
        return Result.success(StudentHolder.getStudent());
    }

    @GetMapping("logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(SESSION_STUDENT_DTO);
        return Result.success();
    }
}
