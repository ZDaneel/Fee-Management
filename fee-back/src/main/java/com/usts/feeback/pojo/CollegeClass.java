package com.usts.feeback.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (CollegeClass)表实体类
 *
 * @author makejava
 * @since 2022-12-10 00:08:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("college_class")
public class CollegeClass implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 班级名
     */
    private String cname;

    /**
     * 创建者
     */
    private Integer creator;

    private static final long serialVersionUID = 1L;
}

