package com.usts.feeback.pojo;

import java.util.*;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属父评论的id 没有为0
     */
    private Integer pid;

    /**
     * 所属fee的id
     */
    private Integer targetId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 作者id
     */
    private Integer studentId;

    /**
     * 作者名字
     */
    @TableField(exist = false)
    private String studentName;

    /**
     * 对谁的回复
     */
    private Integer toStudentId;

    /**
     * 对谁的回复名字
     */
    @TableField(exist = false)
    private String toStudentName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否关闭 0-开启 1-关闭
     */
    private Integer closed;

    /**
     * 子回复列表
     */
    @TableField(exist = false)
    private List<Comment> replyList = new ArrayList<>();

    /**
     * 已确认的学生ids
     */
    @TableField(exist = false)
    private Set<String> confirmIds = new HashSet<>();
    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    private static final long serialVersionUID = 1L;

}

