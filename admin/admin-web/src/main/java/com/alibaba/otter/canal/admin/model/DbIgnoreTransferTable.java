package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库无视扩容表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_ignore_transfer_table")
public class DbIgnoreTransferTable extends Model {

    public static final DbIgnoreTransferTableFinder find = new DbIgnoreTransferTableFinder();

    public static class DbIgnoreTransferTableFinder extends Finder<Long, DbIgnoreTransferTable> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbIgnoreTransferTableFinder(){
            super(DbIgnoreTransferTable.class);
        }

    }

    @Id
    private Long    id;
    private String  tableName;
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
