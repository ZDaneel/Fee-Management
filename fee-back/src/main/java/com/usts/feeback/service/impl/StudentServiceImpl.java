package com.usts.feeback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.StudentMapper;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @author leenadz
 * @since 2022-12-08 00:32
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
