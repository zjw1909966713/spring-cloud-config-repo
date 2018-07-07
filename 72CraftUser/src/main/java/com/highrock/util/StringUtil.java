package com.highrock.util;

import java.util.List;
import java.util.Map;

public class StringUtil {

    /**
     * 是否为空，true为空，false为不是空
     */
    public static boolean isBlank(String str){
        if(str==null||str.length()==0){
            return true;

        }

        return false;
    }






    /**
     *  判断list map 某个字段值是否重复
     * @param list
     * @param field 字段名称
     * @return true表示有重复，false表示未重复
     */
    public  static boolean  hasListMapFieldRepeat(List<Map<String,Object>> list,String field){

        boolean flag = false;


        for (int i = 0; i <list.size()-1; i++) {
            for (int j =i+1; j <list.size() ; j++) {
                if( list.get(j).get(field).toString().equals(list.get(i).get(field).toString())){
                    return  true;
                }

            }

        }



        return false;
    }




}
