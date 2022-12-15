package com.usts.fee_front.pojo;

import androidx.annotation.NonNull;

import java.util.Date;
import java.io.Serializable;

/**
 * (Fee)实体类
 *
 * @author makejava
 * @since 2022-12-10 13:10:59
 */
public class Fee {
    /**
     * 主键
     */
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
    private Integer deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCollegeClassId() {
        return collegeClassId;
    }

    public void setCollegeClassId(Integer collegeClassId) {
        this.collegeClassId = collegeClassId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @NonNull
    @Override
    public String toString() {
        return "Fee{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", money=" + money +
                ", imageUrl='" + imageUrl + '\'' +
                ", noteUrl='" + noteUrl + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", closed=" + closed +
                ", createTime=" + createTime +
                ", creator='" + creator + '\'' +
                ", collegeClassId=" + collegeClassId +
                '}';
    }
}

