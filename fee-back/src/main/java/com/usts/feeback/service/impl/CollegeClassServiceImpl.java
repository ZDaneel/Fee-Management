package com.usts.feeback.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usts.feeback.dao.CollegeClassMapper;
import com.usts.feeback.dto.CollegeClassDTO;
import com.usts.feeback.dto.StudentDTO;
import com.usts.feeback.pojo.CollegeClass;
import com.usts.feeback.service.CollegeClassService;
import com.usts.feeback.utils.Result;
import com.usts.feeback.utils.StudentHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (CollegeClass)表服务实现类
 *
 * @author makejava
 * @since 2022-12-10 00:08:24
 */
@Service()
public class CollegeClassServiceImpl extends ServiceImpl<CollegeClassMapper, CollegeClass> implements CollegeClassService {

    @Resource
    private CollegeClassMapper collegeClassMapper;

    @Override
    public List<CollegeClassDTO> queryClasses() {
        /*
         * 1. 获取当前用户id
         * 2. 通过id查询班级的ids
         * 3. 通过ids查询班级的具体信息
         */
        Integer studentId = StudentHolder.getStudent().getId();
        List<Integer> classIds = collegeClassMapper.queryClassIdsByStudentId(studentId);
        if (classIds.size() == 0) {
            return null;
        }
        List<CollegeClass> collegeClassList = listByIds(classIds);
        List<CollegeClassDTO> collegeClassDTOList = new ArrayList<>();
        collegeClassList.forEach(collegeClass -> {
            CollegeClassDTO collegeClassDTO = BeanUtil.copyProperties(collegeClass, CollegeClassDTO.class);
            Integer number = getStudentCount(collegeClass.getId());
            collegeClassDTO.setNumber(number);
            collegeClassDTOList.add(collegeClassDTO);
        });
        return collegeClassDTOList;
    }

    @Override
    public Integer getStudentCount(Integer classId) {
        return collegeClassMapper.countStudentNumber(classId);
    }
}

