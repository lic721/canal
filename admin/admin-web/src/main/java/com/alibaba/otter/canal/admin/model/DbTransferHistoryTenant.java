package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库触发扩容的租户
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_transfer_history_tenant")
public class DbTransferHistoryTenant extends Model {

    public static final DbTransferHistoryTenantFinder find = new DbTransferHistoryTenantFinder();

    public static class DbTransferHistoryTenantFinder extends Finder<Long, DbTransferHistoryTenant> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbTransferHistoryTenantFinder(){
            super(DbTransferHistoryTenant.class);
        }

    }

    @Id
    private Long   id;
    private String tenantCode;
    private Long   dbTransferHistoryId;
    @WhenCreated
    private Date   creationDate;
    @WhenModified
    private Date   modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Long getDbTransferHistoryId() {
        return dbTransferHistoryId;
    }

    public void setDbTransferHistoryId(Long dbTransferHistoryId) {
        this.dbTransferHistoryId = dbTransferHistoryId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
