package com.alibaba.otter.canal.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.alibaba.otter.canal.admin.dto.DbTransferConfigDTO;
import com.alibaba.otter.canal.admin.model.*;
import com.alibaba.otter.canal.admin.service.DbInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.otter.canal.admin.common.Threads;
import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.connector.MySqlConnectors;
import com.alibaba.otter.canal.admin.model.DbTransferConfig;
import com.alibaba.otter.canal.admin.service.DbTransferConfigService;

import io.ebean.Query;

@Service
public class DbTransferConfigServiceImpl implements DbTransferConfigService {

    @Autowired
    DbInfoService dbInfoService;

    @Override
    public void save(DbTransferConfig dbTransferConfig) {
        int cnt = DbTransferConfig.find.query().where().eq("dbInfoId", dbTransferConfig.getDbInfoId()).findCount();
        if (cnt > 0) {
            throw new ServiceException("该数据库已存在扩容配置");
        }
        dbTransferConfig.save();
    }

    @Override
    public DbTransferConfig detail(Long id) {
        return DbTransferConfig.find.byId(id);
    }

    @Override
    public void update(DbTransferConfig dbTransferConfig) {
        int cnt = DbTransferConfig.find.query()
            .where()
            .eq("dbInfoId", dbTransferConfig.getDbInfoId())
            .ne("id", dbTransferConfig.getId())
            .findCount();
        if (cnt > 0) {
            throw new ServiceException("该数据库已存在扩容配置");
        }

        dbTransferConfig.update("tenantCodeColumnName", "keyTableName", "tableCountThreshold", "dbInfoId");
    }

    @Override
    public void delete(Long id) {
        DbTransferConfig dbTransferConfig = DbTransferConfig.find.byId(id);
        if (dbTransferConfig != null) {
            dbTransferConfig.delete();
        }
    }

    private Query<DbTransferConfig> getBaseQuery(DbTransferConfig dbTransferConfig) {
        Query<DbTransferConfig> query = DbTransferConfig.find.query();
        return query;
    }

    @Override
    public List<DbTransferConfig> findAll(DbTransferConfig dbTransferConfig) {
        Query<DbTransferConfig> query = getBaseQuery(dbTransferConfig);
        query.order().asc("id");
        return query.findList();
    }

    @Override
    public Pager<DbTransferConfigDTO> findList(DbTransferConfig dbTransferConfig, Pager<DbTransferConfigDTO> pager) {

        Query<DbTransferConfig> query = getBaseQuery(dbTransferConfig);
        Query<DbTransferConfig> queryCnt = query.copy();

        int count = queryCnt.findCount();
        pager.setCount((long) count);

        List<DbTransferConfig> dbTransferConfigs = query.order()
            .asc("id")
            .setFirstRow(pager.getOffset().intValue())
            .setMaxRows(pager.getSize())
            .findList();

        List<DbTransferConfigDTO> dtoList = new ArrayList<>(dbTransferConfigs.size());
        for (DbTransferConfig transferConfig : dbTransferConfigs) {
            DbTransferConfigDTO dto = new DbTransferConfigDTO();
            BeanUtils.copyProperties(transferConfig, dto);
            DbInfo dbInfo = dbInfoService.detail(dto.getDbInfoId());
            if (dbInfo != null) {
                dto.setDbIp(dbInfo.getDbIp());
                dto.setDbPort(dbInfo.getDbPort());
                dto.setDbName(dbInfo.getDbName());
            }
            dtoList.add(dto);
        }
        pager.setItems(dtoList);

        return pager;
    }

}
