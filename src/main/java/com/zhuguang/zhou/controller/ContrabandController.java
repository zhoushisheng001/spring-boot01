/**
 * <pre>
 * 作   者：issac.lai
 * 创建日期：2018-4-23
 * </pre>
 */
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

package com.zhuguang.zhou.controller;

import com.zhuguang.zhou.pojo.Contraband;
import com.zhuguang.zhou.pojo.ContrabandBO;
import com.zhuguang.zhou.pojo.Pagination;
import com.zhuguang.zhou.pojo.ResponseData;
import com.zhuguang.zhou.service.ContrabandService;
import com.zhuguang.zhou.serviceInterfac.ContrabandRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 * 违禁品管理表现层控制类
 * </pre>
 */
@RestController
public class ContrabandController implements ContrabandRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(ContrabandController.class);

    @Autowired
    private ContrabandService contrabandService;

    @Override
    public ResponseData<Contraband> get(@RequestBody Contraband contraband) {
        ResponseData resp = new ResponseData();
        logger.info("contraban查询开始:");
        Contraband contraband1 = contrabandService.get(contraband.getId());
        resp.setData(contraband1).ok();
        return resp;
    }

    @Override
    public ResponseData<Integer> save(@RequestBody Contraband contraband) {
        return null;
    }

    @Override
    public ResponseData<Integer> update(@RequestBody Contraband contraband) {
        return null;
    }

    @Override
    public ResponseData<List<Contraband>> list(@RequestBody Contraband contraband) {
        return null;
    }

    @Override
    public ResponseData<Pagination<Contraband>> search(@RequestBody ContrabandBO contrabandBO) {
        Pagination pagination = Pagination.getInstance4BO(contrabandBO);
        ResponseData<Pagination<Contraband>> resp = new ResponseData();
        contrabandService.search(pagination);
        resp.setData(pagination).ok();
        return resp;
    }

    @Override
    public ResponseData<Integer> batchDelete(@RequestBody ContrabandBO contrabandBO) {
        return null;
    }
}