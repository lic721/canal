package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库扩容表的配置表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_transfer_config_table")
public class DbTransferConfigTable extends Model {

    public static final DbTransferConfigTableFinder find = new DbTransferConfigTableFinder();

    public static class DbTransferConfigTableFinder extends Finder<Long, DbTransferConfigTable> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbTransferConfigTableFinder(){
            super(DbTransferConfigTable.class);
        }

    }

    @Id
    private Long    id;
    private String  tableName;
    private Boolean ignoreFlag;
    private Boolean deleteAfterTransfer;
    private Long    dbTransferConfigId;
    @WhenCreated
    private Date    creationDate;
    @WhenModified
    private Date    modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Boolean getIgnoreFlag() {
        return ignoreFlag;
    }

    public void setIgnoreFlag(Boolean ignoreFlag) {
        this.ignoreFlag = ignoreFlag;
    }

    public Boolean getDeleteAfterTransfer() {
        return deleteAfterTransfer;
    }

    public void setDeleteAfterTransfer(Boolean deleteAfterTransfer) {
        this.deleteAfterTransfer = deleteAfterTransfer;
    }

    public Long getDbTransferConfigId() {
        return dbTransferConfigId;
    }

    public void setDbTransferConfigId(Long dbTransferConfigId) {
        this.dbTransferConfigId = dbTransferConfigId;
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
