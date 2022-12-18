package com.usts.feeback;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usts.feeback.pojo.Comment;
import com.usts.feeback.pojo.Fee;
import com.usts.feeback.pojo.Student;
import com.usts.feeback.service.CommentService;
import com.usts.feeback.service.FeeService;
import com.usts.feeback.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CommentService commentService;

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
        fee.setId(2);
        fee.setFname("测试");
        fee.setMoney(10.2);
        fee.setAcceptor("张三");
        fee.setClosed(0);
        fee.setCollegeClassId(1);
        fee.setCreator("李四");
        feeService.insertFee(fee);
        fee.setId(1);
        feeService.insertFee(fee);
    }

    @Test
    void testQueryComment() {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getTargetId, 1).eq(Comment::getPid, 0).select(Comment::getStudentId);
        List<Comment> commentList = commentService.list(lambdaQueryWrapper);
        List<Integer> integerList = commentList.stream().map(Comment::getStudentId).collect(Collectors.toList());
        commentList.forEach(System.out::println);
        integerList.forEach(System.out::println);
    }

    @Test
    void compute() {
        System.out.println(1 / 2);
    }

    @Test
    void testQueryOpenFees() {
        feeService.queryOpenFees(1, "");
    }

    @Test
    void testAddComment() {
        Comment comment = new Comment();
        comment.setId(1);
        commentService.insertParentComment(comment);
        comment.setId(2);
        commentService.insertParentComment(comment);
    }

    @Test
    void testQueryOpenParentComments() {
        commentService.queryOpenParentComments(1, "");
    }

    @Test
    void testQueryCommentByCommentId() {
        commentService.queryParentComment(1);
    }
}
