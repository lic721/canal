package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据库信息表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_info")
public class DbInfo extends Model {

    public static final DbInfoFinder find = new DbInfoFinder();

    public static class DbInfoFinder extends Finder<Long, DbInfo> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbInfoFinder(){
            super(DbInfo.class);
        }

    }

    @Id
    private Long    id;
    private String  dbIp;
    private Integer dbPort;
    private String  dbName;
    private String  dbUserName;
    private String  dbPassword;
    private Boolean backUpFlag;
    private Long    canalInstanceConfigId;
    private Integer sequence;
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

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Boolean getBackUpFlag() {
        return backUpFlag;
    }

    public void setBackUpFlag(Boolean backUpFlag) {
        this.backUpFlag = backUpFlag;
    }

    public Long getCanalInstanceConfigId() {
        return canalInstanceConfigId;
    }

    public void setCanalInstanceConfigId(Long canalInstanceConfigId) {
        this.canalInstanceConfigId = canalInstanceConfigId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
