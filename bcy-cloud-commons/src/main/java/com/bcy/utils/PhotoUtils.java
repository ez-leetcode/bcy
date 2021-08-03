package com.bcy.utils;

import java.util.LinkedList;
import java.util.List;

//用于数据库photo词条与list互相转换
public class PhotoUtils {

    public static String photoListToString(List<String> photos){
        StringBuilder stringBuffer = new StringBuilder();
        for(String x:photos){
            stringBuffer.append(x).append("#");
        }
        return stringBuffer.toString();
    }

    public static List<String> photoStringToList(String photo){
        List<String> stringList = new LinkedList<>();
        int last = 0;
        for(int i = 0 ; i < photo.length(); i++){
            if(photo.charAt(i) == '#'){
                String s = photo.substring(last,i);
                last = i + 1;
                stringList.add(s);
            }
        }
        return stringList;
    }

    public static int getPhotoCountsFromString(String photo){
        int cnt = 0;
        for(int i = 0 ; i < photo.length(); i++){
            if(photo.charAt(i) == '#'){
                cnt ++;
            }
        }
        return cnt;
    }

}