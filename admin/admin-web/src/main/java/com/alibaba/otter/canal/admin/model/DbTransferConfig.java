package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库扩容配置表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_transfer_config")
public class DbTransferConfig extends Model {

    public static final DbTransferConfigFinder find = new DbTransferConfigFinder();

    public static class DbTransferConfigFinder extends Finder<Long, DbTransferConfig> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbTransferConfigFinder(){
            super(DbTransferConfig.class);
        }

    }

    @Id
    private Long   id;
    private String tenantCodeColumnName;
    private String keyTableName;
    private Long   tableCountThreshold;
    private Long   dbInfoId;
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

    public String getTenantCodeColumnName() {
        return tenantCodeColumnName;
    }

    public void setTenantCodeColumnName(String tenantCodeColumnName) {
        this.tenantCodeColumnName = tenantCodeColumnName;
    }

    public String getKeyTableName() {
        return keyTableName;
    }

    public void setKeyTableName(String keyTableName) {
        this.keyTableName = keyTableName;
    }

    public Long getTableCountThreshold() {
        return tableCountThreshold;
    }

    public void setTableCountThreshold(Long tableCountThreshold) {
        this.tableCountThreshold = tableCountThreshold;
    }

    public Long getDbInfoId() {
        return dbInfoId;
    }

    public void setDbInfoId(Long dbInfoId) {
        this.dbInfoId = dbInfoId;
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
