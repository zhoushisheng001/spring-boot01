package com.zhuguang.zhou.common.service;


import java.util.Map;
import java.util.Set;

/**
 *
 */
public interface DataScopeClient {
    /**
     *
     * @return
     */
    public Map<String, Object> getDataScopePermission();

    /**
     * 得到URL对应权限列控制
     * key:all_permission_column 所有权限控制列
     *     my_permission_column  当前登录有权限看见的列
     * @return
     */
    public Map<String,Set<String>> getDataScopePermissionColumn();

}
