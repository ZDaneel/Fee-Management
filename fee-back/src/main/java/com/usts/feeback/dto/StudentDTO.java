package com.usts.feeback.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leenadz
 * @since 2022-12-08 16:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 学号
     */
    private String sno;

    /**
     * 姓名
     */
    private String sname;
}
