package com.usts.feeback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usts.feeback.dto.CollegeClassDTO;
import com.usts.feeback.pojo.CollegeClass;
import com.usts.feeback.utils.Result;

import java.util.List;

/**
 * (CollegeClass)表服务接口
 *
 * @author makejava
 * @since 2022-12-10 00:08:24
 */
public interface CollegeClassService extends IService<CollegeClass> {

    /**
     * 查询当前用户所在的班级
     * @return 班级列表
     */
    List<CollegeClassDTO> queryClasses();

    /**
     * 根据班级id查询班级内人数
     * @param classId 班级id
     * @return 班级内人数
     */
    Integer getStudentCount(Integer classId);
}

