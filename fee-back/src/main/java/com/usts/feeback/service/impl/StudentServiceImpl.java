package com.usts.feeback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.StudentMapper;
import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.usts.feeback.utils.Constants.SESSION_STUDENT_DTO;

/**
 * @author leenadz
 * @since 2022-12-08 00:32
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public Result<Boolean> login(Student student, HttpServletRequest request) {
        String sno = student.getSno();
        String password = student.getPassword();
        if (StrUtil.hasEmpty(sno)) {
            return Result.error("学号为空");
        }
        if (StrUtil.hasEmpty(password)) {
            return Result.error("密码为空");
        }
        // 查询数据库
        Student queryStudent = getOne(
                Wrappers
                        .<Student>lambdaQuery()
                        .eq(Student::getSno, sno)
                        .eq(Student::getPassword, password)
        );
        if (queryStudent == null) {
            return Result.error("学号或密码错误");
        }
        StudentDTO studentDTO = BeanUtil.copyProperties(queryStudent, StudentDTO.class);
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_STUDENT_DTO, studentDTO);
        log.info("请求后返回的sessionId: " + session.getId());
        return Result.success();
    }
}
