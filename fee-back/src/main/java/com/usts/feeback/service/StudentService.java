package com.usts.feeback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.utils.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @author leenadz
 * @since 2022-12-08 00:31
 */
public interface StudentService extends IService<Student> {
    /**
     * 登陆业务
     * @param student 接收到的学生
     * @param request request
     * @return 结果
     */
    Result<Boolean> login(Student student, HttpServletRequest request);
}
