package com.fit.base;

import java.util.List;
import java.util.Map;

/**
 * @AUTO DAO支持类接口
 * @FILE BaseCrudDao.java
 * @DATE 2018-3-23 下午2:38:38
 * @Author AIM
 */
public interface BaseCrudDao<T> {

    /**
     * 获取单条数据
     */
    public T getById(Long id);

    /**
     * 获取单条数据
     */
    public T get(T entity);

    /**
     * 查询数据列表
     */
    public List<T> findList(Object id);

    /**
     * 查询数据列表
     */
    public List<T> findList(Map<String, Object> map);

    /**
     * 列表数量
     */
    public int findCount(Map<String, Object> map);

    /**
     * 插入数据
     */
    public int save(T entity);

    /**
     * 更新数据
     */
    public int update(T entity);

    /**
     * 删除数据
     */
    public int delete(Long id);

    /**
     * 删除数据（一般为逻辑删除，更新del_flag字段为1）
     */
    public int delete(T entity);

    /**
     * 批量删除
     */
    public int batchDelete(String[] ids);

}