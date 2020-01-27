package com.cherlshall.butterfly.common.validate;

public class PredefinedRegex {

    /**
     * 只允许数字
     */
    public static final String ONLY_NUMBER = "^[0-9]*$";

    /**
     * 非零正整数
     */
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

    /**
     * 非负整数
     */
    public static final String INTEGER_NOT_NEGATIVE = "^\\d+$";

    /**
     * 字母数字
     */
    public static final String ENGLISH_NUMBER = "^[a-zA-Z0-9]*$";

    /**
     * 字母数字下划线
     */
    public static final String ENGLISH_NUMBER_UNDERLINE = "^[a-zA-Z0-9_]*$";

    /**
     * 纯中文
     */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]*$";

    /**
     * 用户名(字母开头，允许5-16字节，允许字母数字下划线)
     */
    public static final String USER_NAME = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)
     */
    public static final String PASSWORD = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 手机号
     */
    public static final String PHONE_NUMBER = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

}
