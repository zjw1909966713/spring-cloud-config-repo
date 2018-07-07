package com.highrock.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class JSONHelper {
    /**
     * 把json字符串转换为map
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> convertJsonStrToMap(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>(){});
    }


    /**
     * 把json字符串转换为list
     * @param jsonStr
     * @return
     */
    public static List<Map<String, Object>> convertJsonStrToList(String jsonStr) {
        return JSON.parseObject(jsonStr, new TypeReference<List<Map<String, Object>>>(){});
    }


}
