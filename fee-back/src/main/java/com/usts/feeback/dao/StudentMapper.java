package com.usts.feeback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usts.feeback.pojo.Student;
import org.apache.ibatis.annotations.Param;

/**
 * @author zdaneel
 */
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 根据学生id和班级id查询权限
     * @param studentId 学生id
     * @param classId 班级id
     * @return 权限
     */
    Integer queryRole(@Param("studentId")Integer studentId, @Param("classId")Integer classId);
}