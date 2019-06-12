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



/**
 * <pre>
 * 违禁品管理业务实体类
 * </pre>
 */
public class ContrabandBO extends GenericBO<Contraband> {
    public ContrabandBO() {
        setVo(new Contraband());
    }
    
    public Contraband getContraband() {
        return (Contraband) getVo();
    }
    
    public void setContraband(Contraband vo) {
        setVo(vo);
    }

    /************************   以下字段为扩展属性    ****************************/
    /*//disable未提供更新updateBy,UpdateDate,TraceId，替代方法需用到的新增字段
    private Long[] longIds;

    public Long[] getLongIds() {
        return longIds;
    }

    public void setLongIds(Long[] longIds) {
        this.longIds = longIds;
    }*/
}