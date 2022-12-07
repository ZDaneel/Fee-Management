package com.usts.feeback;

import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author leenadz
 * @since 2022-12-08 00:35
 */
@SpringBootTest
public class ServiceTest {
    @Autowired
    private StudentService studentService;

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setSname("张三");
        student.setSno("12345");
        student.setPassword("666");
        studentService.save(student);
    }
}
