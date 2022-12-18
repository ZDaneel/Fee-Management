package com.usts.feeback.utils;

/**
 * @author leenadz
 * @since 2022-12-09 15:11
 */
public class Constants {
    /**
     * Session存取
     */
    public static final String SESSION_STUDENT_DTO = "studentDTO";

    /**
     * Redis存储
     */
    public static final String COMMENT_KEY = "comment:";

    /**
     * 过期时间
     */
    public static final Long FEE_TTL = 7L;

    /**
     * 是否已经关闭
     */
    public static final int CLOSED = 1;

    /**
     * 用户权限
     */
    public static final int CLASS_MEMBER = 0;
    public static final int CLASS_COMMITTEE = 1;
    public static final int BOOKKEEPER = 2;
}
