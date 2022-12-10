package com.usts.feeback;

import com.usts.feeback.pojo.Fee;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.FeeService;
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

    @Autowired
    private FeeService feeService;

    @Test
    void testAddStudent() {
        Student student = new Student();
        student.setSname("张三");
        student.setSno("12345");
        student.setPassword("666");
        studentService.save(student);
    }

    @Test
    void testAddFee() {
        Fee fee = new Fee();
        fee.setId(4);
        fee.setFname("测试");
        fee.setMoney(10.2);
        fee.setAcceptor("张三");
        fee.setClosed(0);
        fee.setCollegeClassId(1);
        fee.setCreator("李四");
        feeService.insertFee(fee);
        feeService.confirmFee(fee.getId());
        feeService.cancelFee(fee.getId());
    }
}
