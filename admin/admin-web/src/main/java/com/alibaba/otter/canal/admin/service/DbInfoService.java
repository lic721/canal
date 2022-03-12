package com.alibaba.otter.canal.admin.service;

import com.alibaba.otter.canal.admin.model.DbInfo;
import com.alibaba.otter.canal.admin.model.Pager;

import java.util.List;

/**
 * The interface Db info service.
 */
public interface DbInfoService {

    /**
     * Save.
     *
     * @param dbInfo the db info
     */
    void save(DbInfo dbInfo);

    /**
     * Detail db info.
     *
     * @param id the id
     * @return the db info
     */
    DbInfo detail(Long id);

    /**
     * Update.
     *
     * @param dbInfo the db info
     */
    void update(DbInfo dbInfo);

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(Long id);

    /**
     * Find all list.
     *
     * @param dbInfo the db info
     * @return the list
     */
    List<DbInfo> findAll(DbInfo dbInfo);

    /**
     * Find list pager.
     *
     * @param dbInfo the db info
     * @param pager  the pager
     * @return the pager
     */
    Pager<DbInfo> findList(DbInfo dbInfo, Pager<DbInfo> pager);
}
