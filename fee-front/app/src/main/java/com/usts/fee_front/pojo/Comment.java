package com.usts.fee_front.pojo;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2022-12-10 20:20:27
 */
public class Comment {
    /**
     * 主键
     */
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
    private String studentName;

    /**
     * 对谁的回复
     */
    private Integer toStudentId;

    /**
     * 对谁的回复名字
     */
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
    private List<Comment> replyList = new ArrayList<>();

    /**
     * 已确认的学生ids
     */
    private Set<String> confirmIds = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getToStudentId() {
        return toStudentId;
    }

    public void setToStudentId(Integer toStudentId) {
        this.toStudentId = toStudentId;
    }

    public String getToStudentName() {
        return toStudentName;
    }

    public void setToStudentName(String toStudentName) {
        this.toStudentName = toStudentName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }

    public Set<String> getConfirmIds() {
        return confirmIds;
    }

    public void setConfirmIds(Set<String> confirmIds) {
        this.confirmIds = confirmIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", pid=" + pid +
                ", targetId=" + targetId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", toStudentId=" + toStudentId +
                ", toStudentName='" + toStudentName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", closed=" + closed +
                ", replyList=" + replyList +
                ", confirmIds=" + confirmIds +
                '}';
    }
}

