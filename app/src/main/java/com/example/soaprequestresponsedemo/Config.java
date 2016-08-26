package com.example.soaprequestresponsedemo;

public class Config {

    /* ===================== WebService CONST Variable ========================= */
    public static boolean LOG = true;
    public static String SERVER_URL = "http://testdemo.com/servicedemo.asmx";
    public static String NAMESPACE = "http://service.demo.com/";
    public static String METHOD_NAME_GET_REGION = "GetRegion";
    /**
     * This variable holds HTTP CONNECTION TIMEOUT
     */
    public static final int HTTP_TIMEOUT = 240000;

    // response code
    public static final int RESULT_OK = 0;
    public static final int RESULT_NOT_VALID = -1;
    public static final int RESULT_ERROR = 1;
}
