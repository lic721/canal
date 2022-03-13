package com.alibaba.otter.canal.admin.service;

import java.util.List;

import com.alibaba.otter.canal.admin.dto.DbTransferHistoryDTO;
import com.alibaba.otter.canal.admin.model.DbTransferHistory;
import com.alibaba.otter.canal.admin.model.Pager;

/**
 * The interface Db transfer config service.
 */
public interface DbTransferHistoryService {

    /**
     * Save.
     *
     * @param dbTransferHistory the db info
     */
    void save(DbTransferHistory dbTransferHistory);

    /**
     * Detail db info.
     *
     * @param id the id
     * @return the db info
     */
    DbTransferHistory detail(Long id);

    /**
     * Update.
     *
     * @param dbTransferHistory the db info
     */
    void update(DbTransferHistory dbTransferHistory);

    /**
     * Find all list.
     *
     * @param dbTransferHistory the db info
     * @return the list
     */
    List<DbTransferHistory> findAll(DbTransferHistory dbTransferHistory);

    /**
     * Find list pager.
     *
     * @param dbTransferHistory the db info
     * @param pager  the pager
     * @return the pager
     */
    Pager<DbTransferHistoryDTO> findList(DbTransferHistory dbTransferHistory, Pager<DbTransferHistoryDTO> pager);
}
