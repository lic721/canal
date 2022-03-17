package com.alibaba.otter.canal.admin.dto;

import java.util.Date;
import java.util.List;

/**
 * 迁移方案
 * 
 * @author zhihua.li
 */
public class DbTransferPlanDTO {

    private Date         startTime;
    private Long         sourceDbInfoId;
    private Long         targetDbInfoId;
    private Long         canalInstanceConfigId;
    private String       mqTopic;
    /**
     * 迁移的租户编号列表
     */
    private List<String> transferTenantCodeList;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Long getSourceDbInfoId() {
        return sourceDbInfoId;
    }

    public void setSourceDbInfoId(Long sourceDbInfoId) {
        this.sourceDbInfoId = sourceDbInfoId;
    }

    public Long getTargetDbInfoId() {
        return targetDbInfoId;
    }

    public void setTargetDbInfoId(Long targetDbInfoId) {
        this.targetDbInfoId = targetDbInfoId;
    }

    public Long getCanalInstanceConfigId() {
        return canalInstanceConfigId;
    }

    public void setCanalInstanceConfigId(Long canalInstanceConfigId) {
        this.canalInstanceConfigId = canalInstanceConfigId;
    }

    public String getMqTopic() {
        return mqTopic;
    }

    public void setMqTopic(String mqTopic) {
        this.mqTopic = mqTopic;
    }

    public List<String> getTransferTenantCodeList() {
        return transferTenantCodeList;
    }

    public void setTransferTenantCodeList(List<String> transferTenantCodeList) {
        this.transferTenantCodeList = transferTenantCodeList;
    }

    @Override
    public String toString() {
        return "DbTransferPlanDTO{" + "startTime=" + startTime + ", sourceDbInfoId=" + sourceDbInfoId
               + ", targetDbInfoId=" + targetDbInfoId + ", canalInstanceConfigId=" + canalInstanceConfigId
               + ", mqTopic='" + mqTopic + '\'' + ", transferTenantCodeList=" + transferTenantCodeList + '}';
    }
}
