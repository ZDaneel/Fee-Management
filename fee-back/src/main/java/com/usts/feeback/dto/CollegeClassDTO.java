package com.usts.feeback.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leenadz
 * @since 2022-12-10 01:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeClassDTO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 班级名
     */
    private String cname;

    /**
     * 创建者
     */
    private Integer creator;

    /**
     * 学生人数
     */
    private Integer number;
}
