package com.aim.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @AUTO Service服务实现基类
 * @FILE BaseCrudService.java
 * @DATE 2018-3-23 下午2:39:17
 * @Author AIM
 */
@Service
public abstract class BaseCrudService<D extends BaseCrudDao<T>, T extends BaseEntity<T>> {

    /**
     * 持久层对象
     */
    @Autowired
    protected D dao;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public T get(Long id) {
        return dao.getById(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        return dao.get(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    public List<T> findList(Map<String, Object> map) {
        return dao.findList(map);
    }

    /**
     * 查询列表总数量
     */
    public int findCount(Map<String, Object> map) {
        return dao.findCount(map);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     */
    @Transactional
    public void save(T entity) {
        Object obj = entity.getId();
        if (obj == null || "".equals(obj)) {
            dao.save(entity);
        } else {
            dao.update(entity);
        }
    }

    /**
     * 更新数据
     *
     * @param entity
     */
    @Transactional
    public int update(T entity) {
        return dao.update(entity);
    }

    /**
     * 删除数据
     *
     * @param entity
     */
    @Transactional
    public int delete(T entity) {
        return dao.delete(entity);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    @Transactional
    public int delete(Long id) {
        return dao.delete(id);
    }

    /**
     * 删除数据
     *
     * @param ids
     */
    @Transactional
    public int batchDelete(String[] ids) {
        return dao.batchDelete(ids);
    }

}
