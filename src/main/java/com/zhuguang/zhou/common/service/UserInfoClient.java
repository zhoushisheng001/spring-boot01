package com.zhuguang.zhou.common.service;


import com.zhuguang.zhou.pojo.UserInfo;

/**
 * 得到用户的关键信息
 */
public interface UserInfoClient {
    /**
     * 得到用户的信息
     * @param userId
     * @return
     */
    public UserInfo getUserInfoByUserId(String userId);
}
