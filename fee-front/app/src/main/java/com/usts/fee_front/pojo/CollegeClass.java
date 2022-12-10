package com.usts.fee_front.pojo;

import androidx.annotation.NonNull;

import lombok.Data;

/**
 * @author zdaneel
 */
public class CollegeClass {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 名称
     */
    private String cname;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 数量
     */
    private Integer number;

    public CollegeClass() {
    }

    public CollegeClass(String cname, Integer number) {
        this.cname = cname;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @NonNull
    @Override
    public String toString() {
        return "CollegeClass{" +
                "id=" + id +
                ", cname='" + cname + '\'' +
                ", creator='" + creator + '\'' +
                ", number=" + number +
                '}';
    }
}
