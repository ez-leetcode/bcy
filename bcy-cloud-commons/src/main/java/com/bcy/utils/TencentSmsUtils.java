package com.bcy.utils;

import java.util.HashMap;

public class TencentSmsUtils {

    public static final String ACCESS_ID = "AKIDPdbIjtmPkqfCSR9at2uVpsfBRRBepjMixxxxxxx";

    public static final String ACCESS_KEY = "7wUllTu4xaw1pLabNs62i6D3cuzoMcbJxxxxxxxx";

    public static final HashMap<Integer,String> TEMPLATE = new HashMap<>();

    static {
        //注册
        TEMPLATE.put(4,"959486");
        //修改密码
        TEMPLATE.put(2,"959520");
        //找回密码
        TEMPLATE.put(3,"959521");
        //登录
        TEMPLATE.put(1,"959522");
        //改绑手机
        TEMPLATE.put(5,"1081411");
    }

}