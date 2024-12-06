package com.fit.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @AUTO JSON工具类
 * @FILE FastJsonUtil.java
 * @DATE 2018-1-5 下午8:14:46
 * @Author AIM
 */
public class FastJsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(FastJsonUtil.class);

    private static SerializeConfig config;

    static {
        TypeUtils.compatibleWithJavaBean = true; //解决首字符大写变小写
        TypeUtils.compatibleWithFieldName = true;//解决首字符大写,第二字符小写转化问题
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * @param object       需要转换的数据
     * @param prettyFormat 是否序列化
     * @Description json字符串转成为字符串
     */
    public static final String toJSONString(Object object, boolean prettyFormat) {
        return JSONObject.toJSONString(object, prettyFormat);
    }

    /**
     * @param object 数据
     * @Description object转换为JSONArray
     */
    public static final JSONArray objParseJsonArray(Object object) {
        return jsonStrParseJsonArray(toJSONString(object, false));
    }

    /**
     * @param jsonStr json字符串
     * @Description json字符串转换为JSONArray
     */
    public static final JSONArray jsonStrParseJsonArray(String jsonStr) {
        return JSONArray.parseArray(jsonStr);
    }

    /**
     * @param object 数据
     * @Description object转换为JSONObject
     */
    public static final JSONObject objParseJsonObject(Object object) {
        return jsonStrParseJsonObject(toJSONString(object, false));
    }

    /**
     * @param jsonStr json字符串
     * @Description JSONObject
     */
    public static final JSONObject jsonStrParseJsonObject(String jsonStr) {
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * @param jsonStr json字符串
     * @param clazz   class名称
     * @Description json字符串转成为List
     */
    public static <T> List<T> getList(String jsonStr, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        try {
            list = JSONObject.parseArray(jsonStr, clazz);
        } catch (Exception e) {
            logger.error("json字符串转List失败！" + jsonStr, e);
        }
        return list;
    }

    /**
     * @param jsonString json字符串
     * @Description json字符串转换成list<Map>
     */
    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = JSONObject.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            logger.error("json字符串转map失败", e);
        }
        return list;
    }

    /**
     * @param jsonStr json字符串
     * @Description json字符串转换为Map
     */
    public static Map<String, Object> json2Map(String jsonStr) {
        try {
            return JSONObject.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            logger.error("json字符串转换失败！" + jsonStr, e);
        }
        return null;
    }

    public static final <T> List<T> getList(String jsontext, String list_str, Class<T> clazz) {
        JSONObject jsonobj = JSONObject.parseObject(jsontext);
        if (jsonobj == null) {
            return null;
        }
        Object obj = jsonobj.get(list_str);
        if (obj == null) {
            return null;
        }
        // if(obj instanceof JSONObject){}
        if (obj instanceof JSONArray) {
            JSONArray jsonarr = (JSONArray) obj;
            List<T> list = new ArrayList<T>();
            for (int i = 0; i < jsonarr.size(); i++) {
                list.add(jsonarr.getObject(i, clazz));
            }
            return list;
        }
        return null;
    }

    /**
     * @param <T>      -> DepartmentBean
     * @param jsontext -> {"department":{"id":"1","name":"生产部"},"password":"admin",
     *                 "username":"admin"}
     * @param obj_str  -> department
     * @param clazz    -> DepartmentBean
     * @return -> T
     */
    public static final <T> T getObject(String jsontext, String obj_str, Class<T> clazz) {
        JSONObject jsonobj = JSON.parseObject(jsontext);
        if (jsonobj == null) {
            return null;
        }

        Object obj = jsonobj.get(obj_str);
        if (obj == null) {
            return null;
        }

        if (obj instanceof JSONObject) {
            return jsonobj.getObject(obj_str, clazz);
        } else {
            logger.info(obj.getClass() + "");
        }

        return null;
    }

    /**
     * @param <T>
     * @param jsontext ->{"department":{"id":"1","name":"生产部"},"password":"admin",
     *                 "username":"admin"}
     * @param clazz    -> UserBean.class
     * @return -> UserBean
     */
    // 注：传入任意的jsontext,返回的T都不会为null,只是T的属性为null
    public static final <T> T getObject(String jsontext, Class<T> clazz) {
        try {
            T t = JSONObject.parseObject(jsontext, clazz);
            return t;
        } catch (Exception e) {
            logger.error("json字符串转换失败！" + jsontext, e);
        }
        return null;
    }

}
