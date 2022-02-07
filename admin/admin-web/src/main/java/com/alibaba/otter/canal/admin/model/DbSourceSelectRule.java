package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * 数据源选择规则表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "db_source_select_rule")
public class DbSourceSelectRule extends Model {

    public static final DbSourceSelectRuleFinder find = new DbSourceSelectRuleFinder();

    public static class DbSourceSelectRuleFinder extends Finder<Long, DbSourceSelectRule> {

        /**
         * Construct using the default EbeanServer.
         */
        public DbSourceSelectRuleFinder(){
            super(DbSourceSelectRule.class);
        }

    }

    @Id
    private Long   id;
    private String tenantCode;
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

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
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
