/**************************************************************************/
/*                                                                        */
/* Copyright (c) 2017 KYE Company                                       */
/* 跨越速运集团有限公司版权所有                                           */
/*                                                                        */
/* PROPRIETARY RIGHTS of KYE Company are involved in the                */
/* subject matter of this material. All manufacturing, reproduction, use, */
/* and sales rights pertaining to this subject matter are governed by the */
/* license agreement. The recipient of this software implicitly accepts   */
/* the terms of the license.                                              */
/* 本软件文档资料是跨越速运集团有限公司的资产，任何人士阅读和                   */
/* 使用本资料必须获得相应的书面授权，承担保密责任和接受相应的法律约束。                 */
/*                                                                        */
/**************************************************************************/

/**
  * <pre>
  * 作   者：issac.lai
  * 创建日期：2018-4-23
  * </pre>
  */

package com.zhuguang.zhou.pojo;



import java.io.Serializable;

/**
 * <pre>
 * 违禁品管理实体类
 * 数据库表名称：contraband
 * </pre>
 */
public class Contraband extends GenericModel<Long> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段名称：违禁品名称
     * 
     * 数据库字段信息:contraband_name VARCHAR(1024)
     */
    private String contrabandName;

    /**
     * 字段名称：可走陆运否(10.可, 20.否)
     * 
     * 数据库字段信息:landable VARCHAR(32)
     */
    private String landable;

    /**
     * 字段名称：备注说明
     * 
     * 数据库字段信息:remark VARCHAR(2048)
     */
    private String remark;

    /**
     * 字段名称：路由ID
     * 
     * 数据库字段信息:trace_id VARCHAR(32)
     */
    //private String traceId;

    /************************   以下字段为扩展属性    ****************************/

    //查询方式    1,精确查询    2.模糊查询
    private  String  queryMode;

    private String creationDateStr; //创建时间格式化

    private String updationDateStr;//修改时间格式化

    public Contraband() {
    }

    public String getContrabandName() {
        return this.contrabandName;
    }

    public void setContrabandName(String contrabandName) {
        this.contrabandName = contrabandName;
    }

    public String getLandable() {
        return this.landable;
    }

    public void setLandable(String landable) {
        this.landable = landable;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQueryMode() {
        return queryMode;
    }

    public void setQueryMode(String queryMode) {
        this.queryMode = queryMode;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public String getUpdationDateStr() {
        return updationDateStr;
    }

    public void setUpdationDateStr(String updationDateStr) {
        this.updationDateStr = updationDateStr;
    }
}