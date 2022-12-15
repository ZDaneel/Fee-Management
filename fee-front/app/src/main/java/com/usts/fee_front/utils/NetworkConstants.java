package com.usts.fee_front.utils;

/**
 * @author zdaneel
 */
public class NetworkConstants {
    private static final String VIRTUAL_HOST = "10.0.2.2";
    public static final String REAL_HOST = "192.168.2.158";
    private static final String PORT = "8096";
    public static final String BASE_URL = "http://" + VIRTUAL_HOST + ":" + PORT;
    public static final String STUDENT_URL = BASE_URL + "/student";
    public static final String COLLEGE_CLASS_URL = BASE_URL + "/college-class";
    public static final String FEE_URL = BASE_URL + "/fee";
    public static final String COMMENT_URL = BASE_URL + "/comment";
    public static final String FILE_URL = BASE_URL + "/file";

    /**
     * 图片展示
     */
    public static final String GET_IMAGE_URL = BASE_URL + "/images/";

    /**
     * 学生相关
     */
    public static final String LOGIN_URL = STUDENT_URL + "/login";
    public static final String LOGOUT_URL = STUDENT_URL + "/logout";
    public static final String QUERY_ROLE_URL = STUDENT_URL + "/role/";

    /**
     * 班级相关
     */
    public static final String QUERY_CLASSES_URL = COLLEGE_CLASS_URL + "/classes";

    /**
     * 支出相关
     */
    public static final String QUERY_OPEN_FEES_URL = FEE_URL + "/open-fees/";
    public static final String QUERY_CLOSED_FEES_URL = FEE_URL + "/closed-fees/";
    public static final String INSERT_FEE_URL = FEE_URL + "/fee";
    public static final String UPDATE_FEE_URL = FEE_URL + "/update";
    public static final String QUERY_FEE_URL = FEE_URL + "/fee/";
    public static final String QUERY_FEE_STATUS_URL = FEE_URL + "/status/";
    public static final String DELETE_FEE_URL = FEE_URL + "/delete";

    /**
     * 评论相关
     */
    public static final String QUERY_OPEN_COMMENTS_URL = COMMENT_URL + "/open-comments/";
    public static final String QUERY_CLOSED_COMMENTS_URL = COMMENT_URL + "/closed-comments/";
    public static final String INSERT_PARENT_COMMENT_URL = COMMENT_URL + "/parent-comment";
    public static final String QUERY_COMMENT_URL = COMMENT_URL + "/parent-comment/";
    public static final String INSERT_CHILD_COMMENT_URL = COMMENT_URL + "/child-comment";
    public static final String CONFIRM_COMMENT_URL = COMMENT_URL + "/confirm/";

    /**
     * 文件相关
     */
    public static final String QUERY_LOGIN_IMAGE_URL = FILE_URL + "/login-image";
    public static final String UPLOAD_IMAGE = FILE_URL + "/upload";
}
