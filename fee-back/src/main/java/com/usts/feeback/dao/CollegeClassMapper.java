package com.usts.feeback.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usts.feeback.pojo.CollegeClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (CollegeClass)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-10 00:08:21
 */
public interface CollegeClassMapper extends BaseMapper<CollegeClass> {

    /**
     * 通过学生id查询所在班级的ids
     * @param studentId 学生id
     * @return 班级ids
     */
    List<Integer> queryClassIdsByStudentId(@Param("studentId") Integer studentId);

    /**
     * 通过班级id查询学生人数
     * @param classId 班级id
     * @return 学生人数
     */
    Integer countStudentNumber(@Param("classId") Integer classId);
}

