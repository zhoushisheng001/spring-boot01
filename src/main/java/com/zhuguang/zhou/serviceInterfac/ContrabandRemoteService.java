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

package com.zhuguang.zhou.serviceInterfac;
import com.zhuguang.zhou.pojo.Contraband;
import com.zhuguang.zhou.pojo.ContrabandBO;
import com.zhuguang.zhou.pojo.Pagination;
import com.zhuguang.zhou.pojo.ResponseData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <pre>
 * 违禁品管理业务接口
 * </pre>
 */
public interface ContrabandRemoteService {
    
    @RequestMapping(value = "/contraband/get", method = RequestMethod.POST)
    ResponseData<Contraband> get(@RequestBody Contraband contraband);
    
    @RequestMapping(value = "/contraband/save", method = RequestMethod.POST)
    ResponseData<Integer> save(@RequestBody Contraband contraband);
    
    @RequestMapping(value = "/contraband/update", method = RequestMethod.POST)
    ResponseData<Integer> update(@RequestBody Contraband contraband);
    
    @RequestMapping(value = "/contraband/list", method = RequestMethod.POST)
    ResponseData<List<Contraband>> list(@RequestBody Contraband contraband);

    @RequestMapping(value = "/contraband/search", method = RequestMethod.POST)
    ResponseData<Pagination<Contraband>> search(@RequestBody ContrabandBO contrabandBO);

    @RequestMapping(value = "/contraband/batchDelete", method = RequestMethod.POST)
    ResponseData<Integer> batchDelete(@RequestBody ContrabandBO contrabandBO);

}