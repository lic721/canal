package com.alibaba.otter.canal.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.otter.canal.admin.common.Threads;
import com.alibaba.otter.canal.admin.common.exception.ServiceException;
import com.alibaba.otter.canal.admin.connector.AdminConnector;
import com.alibaba.otter.canal.admin.connector.SimpleAdminConnectors;
import com.alibaba.otter.canal.admin.model.*;
import com.alibaba.otter.canal.admin.service.DbInfoService;

import io.ebean.Query;

@Service
public class DbInfoServiceImpl implements DbInfoService {

    @Override
    public void save(DbInfo dbInfo) {
        int cnt = DbInfo.find.query()
            .where()
            .eq("dbIp", dbInfo.getDbIp())
            .eq("dbPort", dbInfo.getDbPort())
            .eq("dbName", dbInfo.getDbName())
            .findCount();
        if (cnt > 0) {
            throw new ServiceException("数据库信息已存在");
        }

        dbInfo.save();
    }

    @Override
    public DbInfo detail(Long id) {
        return DbInfo.find.byId(id);
    }

    @Override
    public void update(DbInfo dbInfo) {
        int cnt = DbInfo.find.query()
            .where()
            .eq("dbIp", dbInfo.getDbIp())
            .eq("dbPort", dbInfo.getDbPort())
            .eq("dbName", dbInfo.getDbName())
            .ne("id", dbInfo.getId())
            .findCount();
        if (cnt > 0) {
            throw new ServiceException("数据库信息已存在");
        }

        dbInfo.update("dbIp", "dbPort", "dbName", "dbUserName", "dbPassword", "sequence");
    }

    @Override
    public void delete(Long id) {
        DbInfo dbInfo = DbInfo.find.byId(id);
        if (dbInfo != null) {
            int sourceCnt = DbTransferHistory.find.query().where().eq("sourceDbInfoId", dbInfo.getId()).findCount();
            if (sourceCnt > 0) {
                throw new ServiceException("扩容历史中存在, 无法删除");
            }

            int targetCnt = DbTransferHistory.find.query().where().eq("targetDbInfoId", dbInfo.getId()).findCount();
            if (targetCnt > 0) {
                throw new ServiceException("扩容历史中存在, 无法删除");
            }

            // 同时删除相关表
            List<Object> dbSourceSelectRuleIds = DbSourceSelectRule.find.query().where().eq("dbInfoId", id).findIds();
            if (dbSourceSelectRuleIds != null) {
                dbSourceSelectRuleIds.forEach(dbSourceSelectRuleId -> {
                    DbSourceSelectRule.find.deleteById(Long.valueOf(dbSourceSelectRuleId.toString()));
                });
            }

            List<Object> dbTransferConfigIds = DbTransferConfig.find.query().where().eq("dbInfoId", id).findIds();
            if (dbTransferConfigIds != null) {
                dbTransferConfigIds.forEach(dbTransferConfigId -> {
                    DbTransferConfig.find.deleteById(Long.valueOf(dbTransferConfigId.toString()));
                });
            }

            dbInfo.delete();
        }
    }

    private Query<DbInfo> getBaseQuery(DbInfo dbInfo) {
        Query<DbInfo> query = DbInfo.find.query();

        if (dbInfo != null) {
            if (StringUtils.isNotEmpty(dbInfo.getDbName())) {
                query.where().like("dbName", "%" + dbInfo.getDbName() + "%");
            }
            if (StringUtils.isNotEmpty(dbInfo.getDbIp())) {
                query.where().eq("dbIp", dbInfo.getDbIp());
            }
        }

        return query;
    }

    @Override
    public List<DbInfo> findAll(DbInfo dbInfo) {
        Query<DbInfo> query = getBaseQuery(dbInfo);
        query.order().asc("id");
        return query.findList();
    }

    @Override
    public Pager<DbInfo> findList(DbInfo dbInfo, Pager<DbInfo> pager) {

        Query<DbInfo> query = getBaseQuery(dbInfo);
        Query<DbInfo> queryCnt = query.copy();

        int count = queryCnt.findCount();
        pager.setCount((long) count);

        List<DbInfo> dbInfos = query.order()
            .asc("id")
            .setFirstRow(pager.getOffset().intValue())
            .setMaxRows(pager.getSize())
            .findList();
        pager.setItems(dbInfos);

        if (dbInfos.isEmpty()) {
            return pager;
        }

        List<Future<Boolean>> futures = new ArrayList<>(dbInfos.size());
        // get all nodes status
        for (DbInfo ns : dbInfos) {
            futures.add(Threads.executorService.submit(() -> {
                boolean status = SimpleAdminConnectors.execute(ns.getIp(), ns.getAdminPort(), AdminConnector::check);
                ns.setStatus(status ? "1" : "0");
                return !status;
            }));
        }
        for (Future<Boolean> f : futures) {
            try {
                f.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException e) {
                // ignore
            } catch (TimeoutException e) {
                break;
            }
        }

        return pager;
    }

    @Override
    public int remoteNodeStatus(String ip, Integer port) {
        boolean result = SimpleAdminConnectors.execute(ip, port, AdminConnector::check);
        return result ? 1 : 0;
    }

    @Override
    public String remoteCanalLog(Long id) {
        DbInfo dbInfo = DbInfo.find.byId(id);
        if (dbInfo == null) {
            return "";
        }
        return SimpleAdminConnectors
            .execute(dbInfo.getIp(), dbInfo.getAdminPort(), adminConnector -> adminConnector.canalLog(100));
    }

    @Override
    public boolean remoteOperation(Long id, String option) {
        DbInfo dbInfo = DbInfo.find.byId(id);
        if (dbInfo == null) {
            return false;
        }
        Boolean result = null;
        if ("start".equals(option)) {
            result = SimpleAdminConnectors.execute(dbInfo.getIp(), dbInfo.getAdminPort(), AdminConnector::start);
        } else if ("stop".equals(option)) {
            result = SimpleAdminConnectors.execute(dbInfo.getIp(), dbInfo.getAdminPort(), AdminConnector::stop);
        } else {
            return false;
        }

        if (result == null) {
            result = false;
        }
        return result;
    }
}
