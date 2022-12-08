package com.usts.fee_front.utils;

/**
 * @author zdaneel
 */
public class NetworkConstants {
    private static final String HOST = "10.0.2.2";
    private static final String PORT = "8096";
    public static final String BASE_URL = "http://"+ HOST +":"+PORT;
    public static final String STUDENT = "/student";

    public static final String LOGIN_URL = BASE_URL + STUDENT + "/login";
}
