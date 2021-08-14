package com.bcy.utils;

//专门给安卓小哥（十三小天使）的工具类
public class BngelUtils {

    public static String getRealFileName(String fileName){
        if(fileName.charAt(0) == '"' && fileName.charAt(fileName.length() - 1) == '"'){
            return fileName.substring(1,fileName.length() - 1);
        }
        return fileName;
    }

}
