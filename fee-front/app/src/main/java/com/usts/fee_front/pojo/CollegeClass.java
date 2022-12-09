package com.usts.fee_front.pojo;

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
}
