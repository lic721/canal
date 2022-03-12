package com.alibaba.otter.canal.admin.service;

import java.util.List;

import com.alibaba.otter.canal.admin.dto.DbTransferConfigDTO;
import com.alibaba.otter.canal.admin.model.DbTransferConfig;
import com.alibaba.otter.canal.admin.model.Pager;

/**
 * The interface Db transfer config service.
 */
public interface DbTransferConfigService {

    /**
     * Save.
     *
     * @param dbTransferConfig the db info
     */
    void save(DbTransferConfig dbTransferConfig);

    /**
     * Detail db info.
     *
     * @param id the id
     * @return the db info
     */
    DbTransferConfig detail(Long id);

    /**
     * Update.
     *
     * @param dbTransferConfig the db info
     */
    void update(DbTransferConfig dbTransferConfig);

    /**
     * Delete.
     *
     * @param id the id
     */
    void delete(Long id);

    /**
     * Find all list.
     *
     * @param dbTransferConfig the db info
     * @return the list
     */
    List<DbTransferConfig> findAll(DbTransferConfig dbTransferConfig);

    /**
     * Find list pager.
     *
     * @param dbTransferConfig the db info
     * @param pager  the pager
     * @return the pager
     */
    Pager<DbTransferConfigDTO> findList(DbTransferConfig dbTransferConfig, Pager<DbTransferConfigDTO> pager);
}
