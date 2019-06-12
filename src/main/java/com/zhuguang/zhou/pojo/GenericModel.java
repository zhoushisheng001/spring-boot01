package com.zhuguang.zhou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 通用实体对象
 * @author liheng
 * @since 1.0
 */
public class GenericModel<PK> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected PK id;

    /**
     * 日志跟踪id
     */
    protected String traceId;

    protected String createdBy;

    protected Timestamp creationDate;

    protected String updatedBy;

    protected Timestamp updationDate;

    /**
     * 是否可用
     */
    //@JsonIgnore
    protected Long enabledFlag = 1L;


    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    //@Transient
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(Timestamp updationDate) {
        this.updationDate = updationDate;
    }

    public Long getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Long enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("id='").append(getId()).append("'");
        buffer.append("]");

        return buffer.toString();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof GenericModel)) {
            return false;
        }

        GenericModel other = (GenericModel) o;
        if (getId() != null && other.getId() != null) {
            if (getId() instanceof Comparable) {
                return ((Comparable) getId()).compareTo(other.getId()) == 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {

        int result = 17;

        if (getId() instanceof Comparable) {
            result = getId().hashCode();
        }

        return result;
    }
}