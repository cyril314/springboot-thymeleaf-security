package com.fit.base;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @Author AIM
 * @Des 返回信息对象
 * @DATE 2018/1/31
 */
@Data
public class R {
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回内容
     */
    private Object data;
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 返回数据总数
     */
    private int count = -1;

    public R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static R getInstance(int code, String msg, Object data) {
        return new R(code, msg, data);
    }

    public static R success(Object data, int count) {
        R r = new R(0, null, data);
        r.setCount(count);
        return r;
    }

    public static R success(String msg, Object data) {
        return getInstance(0, msg, data);
    }

    public static R success(String msg) {
        return success(msg, null);
    }

    public static R error(String msg) {
        return getInstance(-1, msg, null);
    }

    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
