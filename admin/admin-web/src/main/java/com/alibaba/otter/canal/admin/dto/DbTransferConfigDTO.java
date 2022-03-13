package com.alibaba.otter.canal.admin.dto;

import com.alibaba.otter.canal.admin.model.DbIgnoreTransferTable;
import com.alibaba.otter.canal.admin.model.DbTransferConfig;

import java.util.List;

/**
 * @author zhihua.li
 */
public class DbTransferConfigDTO extends DbTransferConfig {

    private String                      dbIp;
    private Integer                     dbPort;
    private String                      dbName;
    private List<DbIgnoreTransferTable> ignoreTransferTables;

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<DbIgnoreTransferTable> getIgnoreTransferTables() {
        return ignoreTransferTables;
    }

    public void setIgnoreTransferTables(List<DbIgnoreTransferTable> ignoreTransferTables) {
        this.ignoreTransferTables = ignoreTransferTables;
    }

    @Override
    public String toString() {
        return "DbTransferConfigDTO{" + "dbIp='" + dbIp + '\'' + ", dbPort=" + dbPort + ", dbName='" + dbName + '\''
               + ", ignoreTransferTables=" + ignoreTransferTables + "} " + super.toString();
    }
}
