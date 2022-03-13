package com.alibaba.otter.canal.admin.dto;

import java.util.List;

import com.alibaba.otter.canal.admin.model.*;

/**
 * @author zhihua.li
 */
public class DbTransferHistoryDTO extends DbTransferHistory {

    private String                        sourceDbIp;
    private Integer                       sourceDbPort;
    private String                        sourceDbName;
    private String                        targetDbIp;
    private Integer                       targetDbPort;
    private String                        targetDbName;
    private String                        canalInstanceName;
    private List<DbTransferHistoryTable>  historyTables;
    private List<DbTransferHistoryTenant> historyTenants;

    public String getSourceDbIp() {
        return sourceDbIp;
    }

    public void setSourceDbIp(String sourceDbIp) {
        this.sourceDbIp = sourceDbIp;
    }

    public Integer getSourceDbPort() {
        return sourceDbPort;
    }

    public void setSourceDbPort(Integer sourceDbPort) {
        this.sourceDbPort = sourceDbPort;
    }

    public String getSourceDbName() {
        return sourceDbName;
    }

    public void setSourceDbName(String sourceDbName) {
        this.sourceDbName = sourceDbName;
    }

    public String getTargetDbIp() {
        return targetDbIp;
    }

    public void setTargetDbIp(String targetDbIp) {
        this.targetDbIp = targetDbIp;
    }

    public Integer getTargetDbPort() {
        return targetDbPort;
    }

    public void setTargetDbPort(Integer targetDbPort) {
        this.targetDbPort = targetDbPort;
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        this.targetDbName = targetDbName;
    }

    public String getCanalInstanceName() {
        return canalInstanceName;
    }

    public void setCanalInstanceName(String canalInstanceName) {
        this.canalInstanceName = canalInstanceName;
    }

    public List<DbTransferHistoryTable> getHistoryTables() {
        return historyTables;
    }

    public void setHistoryTables(List<DbTransferHistoryTable> historyTables) {
        this.historyTables = historyTables;
    }

    public List<DbTransferHistoryTenant> getHistoryTenants() {
        return historyTenants;
    }

    public void setHistoryTenants(List<DbTransferHistoryTenant> historyTenants) {
        this.historyTenants = historyTenants;
    }

    @Override
    public String toString() {
        return "DbTransferHistoryDTO{" +
                "sourceDbIp='" + sourceDbIp + '\'' +
                ", sourceDbPort=" + sourceDbPort +
                ", sourceDbName='" + sourceDbName + '\'' +
                ", targetDbIp='" + targetDbIp + '\'' +
                ", targetDbPort=" + targetDbPort +
                ", targetDbName='" + targetDbName + '\'' +
                ", canalInstanceName='" + canalInstanceName + '\'' +
                ", historyTables=" + historyTables +
                ", historyTenants=" + historyTenants +
                "} " + super.toString();
    }
}
