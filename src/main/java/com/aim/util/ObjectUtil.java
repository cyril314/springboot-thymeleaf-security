package com.aim.util;

/**
 * @AUTO 对象工具类
 * @FILE ObjectUtil.java
 * @DATE 2018-1-6 上午1:27:50
 * @Author AIM
 */
public class ObjectUtil {

    /**
     * <将obj转换为string，如果obj为null则返回defaultVal>
     *
     * @param obj        需要转换为string的对象
     * @param defaultVal 默认值
     */
    public static String toString(Object obj, String defaultVal) {
        return (obj != null) ? obj.toString().trim() : defaultVal;
    }

    /**
     * <将obj转换为string，默认为空>
     *
     * @param obj 需要转换为string的对象
     */
    public static String toString(Object obj) {
        return toString(obj, "");
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     */
    public static Integer toInt(Object obj, Integer defaultVal) {
        try {
            return (obj != null) ? Integer.parseInt(toString(obj, "0")) : defaultVal;
        } catch (Exception e) {
        }
        return defaultVal;
    }

    /**
     * <将对象转换为int>
     *
     * @param obj 需要转换为int的对象
     */
    public static Integer toInt(Object obj) {
        return toInt(obj, 0);
    }

    /**
     * <将对象转换为Integer>
     *
     * @param obj 需要转换为Integer的对象
     * @return obj转换成的Integer值
     */
    public static Integer toInteger(Object obj) {
        return toInt(obj, null);
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     */
    public static Float toFloat(Object obj, float defaultVal) {
        return (obj != null) ? Float.parseFloat(toString(obj, "0")) : defaultVal;
    }

    /**
     * <将对象转换为Float>
     *
     * @param obj 需要转换为Float的对象
     */
    public static Float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * <将obj转换为long>
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     */
    public static Long toLong(Object obj, long defaultVal) {
        return (obj != null) ? Long.parseLong(toString(obj)) : defaultVal;
    }

    /**
     * <将obj转换为long>
     *
     * @param obj 需要转换的对象
     */
    public static Long toLong(Object obj) {
        return toLong(obj, 0l);
    }

    /**
     * 将object转换为double类型，如果出错则返回 defaultVal
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     */
    public static Double toDouble(Object obj, Double defaultVal) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 将object转换为double类型，如果出错则返回 0d
     *
     * @param obj 需要转换的对象
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0d);
    }

}
