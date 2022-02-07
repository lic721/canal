package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库触发扩容的表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_transfer_history_table")
public class DbTransferHistoryTable extends Model {

    public static final DbTransferHistoryTableFinder find = new DbTransferHistoryTableFinder();

    public static class DbTransferHistoryTableFinder extends Finder<Long, DbTransferHistoryTable> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbTransferHistoryTableFinder(){
            super(DbTransferHistoryTable.class);
        }

    }

    @Id
    private Long   id;
    private String tableName;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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
