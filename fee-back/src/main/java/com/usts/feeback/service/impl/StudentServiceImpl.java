package com.usts.feeback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.StudentMapper;
import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private StudentMapper studentMapper;

    @Override
    public Result<StudentDTO> login(Student student, HttpServletRequest request) {
        String sno = student.getSno();
        String password = student.getPassword();
        if (StrUtil.hasEmpty(sno)) {
            return Result.error("学号为空");
        }
        if (StrUtil.hasEmpty(password)) {
            return Result.error("密码为空");
        }
        /*
         * 分批次查询数据库
         *      先通过学号查询是否存在,如果不存在就注册
         *      再通过学号和密码查询
         */
        Student queryStudent;
        queryStudent = getOne(
                Wrappers
                        .<Student>lambdaQuery()
                        .eq(Student::getSno, sno)
        );
        if (null == queryStudent) {
            student.setSname("学生" + RandomUtil.randomString(6));
            save(student);
            queryStudent = BeanUtil.copyProperties(student, Student.class);
        } else {
            queryStudent = getOne(
                    Wrappers
                            .<Student>lambdaQuery()
                            .eq(Student::getSno, sno)
                            .eq(Student::getPassword, password)
            );
            if (null == queryStudent) {
                return Result.error("学号或密码错误");
            }
        }
        StudentDTO studentDTO = BeanUtil.copyProperties(queryStudent, StudentDTO.class);
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_STUDENT_DTO, studentDTO);
        return Result.success(studentDTO);
    }

    @Override
    public Integer queryRole(Integer classId) {
        Integer studentId = StudentHolder.getStudent().getId();
        return studentMapper.queryRole(studentId, classId);

    }
}
