package com.highrock.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间的工具
 * @author zhangjinwen
 * @create 2017-11-06 14:51
 * @desc
 **/
public class TimeUtil {


    public static final String DATE_DEFAULT_FORMAT="yyyy-MM-dd HH:mm:ss";//默认的时间格式


    /**
     * 把Unix时间戳转换为指定格式的时间
     * @param timestampString UNIX时间戳（）
     * @param formats 格式化格式 例如"yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String TimeStamp2Date(String timestampString, String formats){
        Long timestamp = Long.parseLong(timestampString)*1000;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    public static void main(String[] args) {
        String a= TimeUtil.TimeStamp2Date("1516679553000","yyyy-MM-dd HH:mmm:ss");
        System.out.println(a);
    }

    /**
     * 把date时间转换成str 格式
     * @param date
     * @param format
     * @return
     */
    public  static String Date2DateStr(Date date,String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String s=sdf.format(date);
        return s;
    }
}
