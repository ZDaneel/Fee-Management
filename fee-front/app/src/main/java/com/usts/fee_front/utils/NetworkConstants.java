package com.usts.fee_front.utils;

/**
 * @author zdaneel
 */
public class NetworkConstants {
    private static final String VIRTUAL_HOST = "10.0.2.2";
    public static final String REAL_HOST = "192.168.2.158";
    private static final String PORT = "8096";
    public static final String BASE_URL = "http://"+ VIRTUAL_HOST +":"+PORT;
    public static final String STUDENT = "/student";
    public static final String COLLEGE_CLASS = "/college-class";
    public static final String FEE = "/fee";
    public static final String COMMENT = "/comment";

    /**
     * 学生相关
     */
    public static final String LOGIN_URL = BASE_URL + STUDENT + "/login";
    public static final String LOGOUT_URL = BASE_URL + STUDENT + "/logout";
    public static final String QUERY_ROLE_URL = BASE_URL + STUDENT + "/role/";

    /**
     * 班级相关
     */
    public static final String QUERY_CLASSES_URL = BASE_URL + COLLEGE_CLASS + "/classes";

    /**
     * 支出相关
     */
    public static final String QUERY_OPEN_FEES_URL = BASE_URL + FEE + "/open-fees/";
    public static final String QUERY_CLOSED_FEES_URL = BASE_URL + FEE + "/closed-fees/";
    public static final String INSERT_FEE_URL = BASE_URL + FEE + "/fee";
    public static final String QUERY_FEE_URL = BASE_URL + FEE + "/fee/";
    public static final String QUERY_FEE_STATUS_URL = BASE_URL + FEE + "/status/";

    /**
     * 评论相关
     */
    public static final String QUERY_OPEN_COMMENTS_URL = BASE_URL + COMMENT + "/open-comments/";
    public static final String INSERT_PARENT_COMMENT_URL = BASE_URL + COMMENT + "/parent-comment";
    public static final String QUERY_COMMENT_URL = BASE_URL + COMMENT + "/parent-comment/";
    public static final String INSERT_CHILD_COMMENT_URL = BASE_URL + COMMENT + "/child-comment";
    public static final String CONFIRM_COMMENT_URL = BASE_URL + COMMENT + "/confirm/";
}
