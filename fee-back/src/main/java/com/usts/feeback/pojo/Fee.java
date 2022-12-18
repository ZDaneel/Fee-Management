package com.usts.feeback.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (Fee)实体类
 *
 * @author makejava
 * @since 2022-12-10 13:10:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("fee")
public class Fee implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 支出名称
     */
    private String fname;
    /**
     * 支出金额
     */
    private Double money;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 小票
     */
    private String noteUrl;
    /**
     * 验收人
     */
    private String acceptor;
    /**
     * 是否关闭 0-开启 1-关闭
     */
    private Integer closed;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 所在班级id
     */
    private Integer collegeClassId;
    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    private static final long serialVersionUID = 1L;
}

