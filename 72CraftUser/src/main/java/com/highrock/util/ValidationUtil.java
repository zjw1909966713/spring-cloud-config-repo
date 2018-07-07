package com.highrock.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证求字段工具类
 * @author zhangjinwen
 * @create 2017-11-01 11:25
 * @desc
 **/
public class ValidationUtil {


    /**
     * 判断缺失那些字段
     * @param requiredFields
     * @param map
     * @return
     */
    public static  String  checkRequiredFields(String[] requiredFields, Map<String,Object> map){

        String ret="";
        List<String> missRequiredFields=new ArrayList<>();

        for (String filed:requiredFields){
            if(!map.containsKey(filed)){//如果不包含key值
                missRequiredFields.add(filed);
            }
        }
       if(missRequiredFields.size()>0){
            StringBuffer sb=new StringBuffer();
            for(String str:missRequiredFields){
                sb.append(str).append(",");
            }

            sb.append(HRContants.MISSING);
           ret=sb.toString();
       }

     return ret;
    }

    /**
     * 验证字符是否匹配正则表达式
     * @param rex 正则表达式
     * @param str 字符串
     * @return
     */
    public static boolean matcher(String rex,String str){

        Pattern pattern = Pattern.compile(rex);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }


}
