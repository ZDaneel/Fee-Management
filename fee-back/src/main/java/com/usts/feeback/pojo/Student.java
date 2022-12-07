package com.usts.feeback.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * college_student
 * @author zdaneel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("college_student")
public class Student implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String sno;

    /**
     * 姓名
     */
    private String sname;

    /**
     * 密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}