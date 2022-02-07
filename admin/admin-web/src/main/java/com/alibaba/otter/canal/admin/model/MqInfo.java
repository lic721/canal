package com.alibaba.otter.canal.admin.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

/**
 * mq信息表
 *
 * @author zhihua.li
 * @version 1.0.0
 */
@Entity
@Table(name = "mq_info")
public class MqInfo extends Model {

    public static final MqInfoFinder find = new MqInfoFinder();

    public static class MqInfoFinder extends Finder<Long, MqInfo> {

        /**
         * Construct using the default EbeanServer.
         */
        public MqInfoFinder(){
            super(MqInfo.class);
        }

    }

    @Id
    private Long   id;
    private String namesrvAddress;
    private String tag;
    private Long   canalConfigId;
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

    public String getNamesrvAddress() {
        return namesrvAddress;
    }

    public void setNamesrvAddress(String namesrvAddress) {
        this.namesrvAddress = namesrvAddress;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCanalConfigId() {
        return canalConfigId;
    }

    public void setCanalConfigId(Long canalConfigId) {
        this.canalConfigId = canalConfigId;
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
