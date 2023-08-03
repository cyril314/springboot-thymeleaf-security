package com.aim.base;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @AUTO Bean基类
 * @FILE BaseEntity.java
 * @DATE 2018-3-23 下午2:40:09
 * @Author AIM
 */
public abstract class BaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 实体编号（唯一标识）
     */
    protected Long id;

    /**
     * 创建时间 (无默认值)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date ctime;

    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}
