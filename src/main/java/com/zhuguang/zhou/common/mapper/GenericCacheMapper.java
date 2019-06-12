package com.zhuguang.zhou.common.mapper;

/***
 * 通用Cache DAO基础类
 * @author liheng
 * @since 1.0
 */
public interface GenericCacheMapper<T, PK> extends GenericMapper {

 /*   *//**
     * 通过Model获取数据
     * @param data  Model数据，非空字段都做为条件查询
     * @return    数据列表
     *//*
    List<PK> selectAllPK(T data);

    *//**
     * 通过pagination对象进行相关参数查询，获取分页（或Top n）数据
     * @param pagination
     * @return    数据列表
     *//*
    List<PK> searchPagePK(Pagination<T> pagination);*/
}
