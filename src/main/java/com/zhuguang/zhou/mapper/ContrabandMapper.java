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

package com.zhuguang.zhou.mapper;

import com.zhuguang.zhou.common.mapper.GenericMapper;
import com.zhuguang.zhou.pojo.Contraband;
import com.zhuguang.zhou.pojo.ContrabandBO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <pre>
 * 违禁品管理数据访问接口
 * </pre>
 */
@Mapper
public interface ContrabandMapper extends GenericMapper<Contraband, Long> {

    /**
     * 批量逻辑删除（带删除日志）
     * @param contrabandBO
     * @return
     */
    public int batchDisable(ContrabandBO contrabandBO);

    /**
     * 校验违禁品名称相同的数据是否已存在
     * @param contraband
     * @return
     */
    Integer checkContrabandExist(Contraband contraband);

    /**
     * 批量根据违禁品查询信息
     * @param contrabandList
     * @return
     */
    List<Contraband> queryList(List<Contraband> contrabandList);

}