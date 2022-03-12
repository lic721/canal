package com.alibaba.otter.canal.admin.dto;

import com.alibaba.otter.canal.admin.model.DbTransferConfig;

/**
 * @author zhihua.li
 */
public class DbTransferConfigDTO extends DbTransferConfig {

    private String  dbIp;
    private Integer dbPort;
    private String  dbName;

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

    @Override
    public String toString() {
        return "DbTransferConfigDTO{" + "dbIp='" + dbIp + '\'' + ", dbPort=" + dbPort + ", dbName='" + dbName + '\''
               + "} " + super.toString();
    }
}
