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

package com.zhuguang.zhou.service;

import com.zhuguang.zhou.common.service.GenericService;
import com.zhuguang.zhou.mapper.ContrabandMapper;
import com.zhuguang.zhou.pojo.Contraband;
import com.zhuguang.zhou.pojo.ContrabandBO;
import com.zhuguang.zhou.pojo.Pagination;
import com.zhuguang.zhou.pojo.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <pre>
 * 违禁品管理业务类
 * </pre>
 */
@Service("contrabandService")
public class ContrabandService extends GenericService<Contraband, Long> {
    public ContrabandService(@Autowired ContrabandMapper contrabandMapper) {
        super(contrabandMapper);
    }
    
    public ContrabandMapper getMapper() {
        return (ContrabandMapper) super.genericMapper;
    }

    public Integer batchDelete(ContrabandBO contrabandBO){
        //校验参数
        return null;
    }

    public ResponseData<List<Contraband>> listByContrabandName(Contraband contraband) {
        ResponseData<List<Contraband>> resp = new ResponseData<>();
        resp.setData(this.selectAll(contraband)).ok();
        return resp;
    }

    public ResponseData<Pagination<Contraband>> searchByContrabandNameBlur(ContrabandBO contrabandBO) {

        Contraband contraband = contrabandBO.getContraband();

        ResponseData<Pagination<Contraband>> resp = new ResponseData<>();
        Pagination<Contraband> pagination = Pagination.getInstance4BO(contrabandBO);
        this.search(pagination);
        resp.setData(pagination).ok();
        return resp;
    }

    /**
     * 批量根据违禁品查询信息
     * @param contrabandList
     * @return
     */
    public List<Contraband> queryList(List<Contraband> contrabandList) {
        return this.getMapper().queryList(contrabandList);
    }
}