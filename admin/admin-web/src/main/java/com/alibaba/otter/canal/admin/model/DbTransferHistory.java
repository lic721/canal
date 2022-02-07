package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库扩容历史记录表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_transfer_history")
public class DbTransferHistory extends Model {

    public static final DbTransferHistoryFinder find = new DbTransferHistoryFinder();

    public static class DbTransferHistoryFinder extends Finder<Long, DbTransferHistory> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbTransferHistoryFinder(){
            super(DbTransferHistory.class);
        }

    }

    @Id
    private Long   id;
    private Date   start_time;
    private Date   end_time;
    private Long   sourceDbInfoId;
    private Long   targetDbInfoId;
    private String dbTransferConfigJson;
    private String remark;
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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
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

    public String getDbTransferConfigJson() {
        return dbTransferConfigJson;
    }

    public void setDbTransferConfigJson(String dbTransferConfigJson) {
        this.dbTransferConfigJson = dbTransferConfigJson;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
