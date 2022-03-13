package com.alibaba.otter.canal.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.otter.canal.admin.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.dto.DbTransferHistoryDTO;
import com.alibaba.otter.canal.admin.service.DbInfoService;
import com.alibaba.otter.canal.admin.service.DbTransferHistoryService;

import io.ebean.Query;

@Service
public class DbTransferHistoryServiceImpl implements DbTransferHistoryService {

    @Autowired
    DbInfoService dbInfoService;

    @Override
    public void save(DbTransferHistory dbTransferHistory) {
        int cnt = DbTransferHistory.find.query()
            .where()
            .eq("sourceDbInfoId", dbTransferHistory.getSourceDbInfoId())
            .isNull("end_time")
            .findCount();
        if (cnt > 0) {
            throw new ServiceException("存在运行中的扩容");
        }
        dbTransferHistory.save();
    }

    @Override
    public DbTransferHistory detail(Long id) {
        return DbTransferHistory.find.byId(id);
    }

    @Override
    public void update(DbTransferHistory dbTransferHistory) {
        dbTransferHistory.update("startTime",
            "endTime",
            "sourceDbInfoId",
            "targetDbInfoId",
            "dbTransferConfigJson",
            "remark",
            "canalInstanceConfigId",
            "mqTopic");
    }

    private Query<DbTransferHistory> getBaseQuery(DbTransferHistory dbTransferHistory) {
        Query<DbTransferHistory> query = DbTransferHistory.find.query();
        return query;
    }

    @Override
    public List<DbTransferHistory> findAll(DbTransferHistory dbTransferHistory) {
        Query<DbTransferHistory> query = getBaseQuery(dbTransferHistory);
        query.order().desc("id");
        return query.findList();
    }

    @Override
    public Pager<DbTransferHistoryDTO> findList(DbTransferHistory dbTransferHistory,
                                                Pager<DbTransferHistoryDTO> pager) {

        Query<DbTransferHistory> query = getBaseQuery(dbTransferHistory);
        Query<DbTransferHistory> queryCnt = query.copy();

        int count = queryCnt.findCount();
        pager.setCount((long) count);

        List<DbTransferHistory> dbTransferHistoryList = query.order()
            .desc("id")
            .setFirstRow(pager.getOffset().intValue())
            .setMaxRows(pager.getSize())
            .findList();

        List<DbTransferHistoryDTO> dtoList = new ArrayList<>(dbTransferHistoryList.size());
        for (DbTransferHistory transferHistory : dbTransferHistoryList) {
            DbTransferHistoryDTO dto = new DbTransferHistoryDTO();
            BeanUtils.copyProperties(transferHistory, dto);
            DbInfo sourceDbInfo = dbInfoService.detail(dto.getSourceDbInfoId());
            if (sourceDbInfo != null) {
                dto.setSourceDbIp(sourceDbInfo.getDbIp());
                dto.setSourceDbPort(sourceDbInfo.getDbPort());
                dto.setSourceDbName(sourceDbInfo.getDbName());
            }
            DbInfo targetDbInfo = dbInfoService.detail(dto.getTargetDbInfoId());
            if (targetDbInfo != null) {
                dto.setTargetDbIp(targetDbInfo.getDbIp());
                dto.setTargetDbPort(targetDbInfo.getDbPort());
                dto.setTargetDbName(targetDbInfo.getDbName());
            }

            CanalInstanceConfig canalInstanceConfig = CanalInstanceConfig.find
                .byId(transferHistory.getCanalInstanceConfigId());
            if (canalInstanceConfig != null) {
                dto.setCanalInstanceName(canalInstanceConfig.getName());
            }
            // 触发扩容的表
            List<DbTransferHistoryTable> historyTables = DbTransferHistoryTable.find.query()
                .where()
                .eq("dbTransferHistoryId", transferHistory.getId())
                .findList();
            dto.setHistoryTables(historyTables);

            // 触发扩容的租户
            List<DbTransferHistoryTenant> historyTenants = DbTransferHistoryTenant.find.query()
                .where()
                .eq("dbTransferHistoryId", transferHistory.getId())
                .findList();
            dto.setHistoryTenants(historyTenants);

            dtoList.add(dto);
        }
        pager.setItems(dtoList);

        return pager;
    }

}
