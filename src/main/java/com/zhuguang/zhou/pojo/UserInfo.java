//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuguang.zhou.pojo;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String KEY_PREFIX = "X-";
    public static final String KEY_ID = "uid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_TYPE = "type";
    public static final String KEY_FROM = "from";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_IP = "ipAddr";
    public static final String ENTRY_FROM_SELLER = "BB";
    public static final String ENTRY_FROM_BUYER = "CF";
    public static final String ENTRY_FROM_OPERATOR = "OB";
    public static final String FLAG_SELLER = "B";
    private String id;
    private String userNumber;
    private String username;
    private String userNameEn;
    private String nickname;
    private Long deptId;
    private String deptName;
    private String position;
    private Long deptDutyUserId;
    private String deptDutyUserName;
    private Long netWorkId;
    private String netWorkName;
    private String type;
    private String from;
    private String phone;
    private String ipAddr;

    public UserInfo() {
    }

    public boolean isSeller() {
        return "BB".equals(this.from);
    }

    public boolean isSeller4FrontEnd() {
        return "CF".equals(this.from) && "B".equals(this.type);
    }

    public boolean isOperator() {
        return "OB".equals(this.from);
    }

    public boolean isBuyer() {
        return "CF".equals(this.from);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public static String getHeaderName(String key) {
        return "X-" + key;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getUserNameEn() {
        return this.userNameEn;
    }

    public UserInfo setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
        return this;
    }

    public Long getDeptId() {
        return this.deptId;
    }

    public UserInfo setDeptId(Long deptId) {
        this.deptId = deptId;
        return this;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public UserInfo setDeptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public String getPosition() {
        return this.position;
    }

    public UserInfo setPosition(String position) {
        this.position = position;
        return this;
    }

    public Long getDeptDutyUserId() {
        return this.deptDutyUserId;
    }

    public UserInfo setDeptDutyUserId(Long deptDutyUserId) {
        this.deptDutyUserId = deptDutyUserId;
        return this;
    }

    public String getDeptDutyUserName() {
        return this.deptDutyUserName;
    }

    public UserInfo setDeptDutyUserName(String deptDutyUserName) {
        this.deptDutyUserName = deptDutyUserName;
        return this;
    }

    public String getUserNumber() {
        return this.userNumber;
    }

    public UserInfo setUserNumber(String userNumber) {
        this.userNumber = userNumber;
        return this;
    }

    public Long getNetWorkId() {
        return this.netWorkId;
    }

    public UserInfo setNetWorkId(Long netWorkId) {
        this.netWorkId = netWorkId;
        return this;
    }

    public String getNetWorkName() {
        return this.netWorkName;
    }

    public UserInfo setNetWorkName(String netWorkName) {
        this.netWorkName = netWorkName;
        return this;
    }
}
