//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuguang.zhou.pojo;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    public static final String DATASCOPE = "dataScope";
    public static final String PERMISSION_ID = "X-PERMISSIONID";
    private String userId;
    private String url;
    private String permissionId;
    private String ipAddress;
    private String traceId;
    private String sessionId;
    private UserInfo user;
    private ClientInfo clientInfo;
    private Boolean exportFlag = false;
    private ConcurrentHashMap<String, Object> globalVariableMap = new ConcurrentHashMap();

    public Context() {
    }

    public ClientInfo getClientInfo() {
        if (this.clientInfo == null) {
            this.clientInfo = new ClientInfo();
        }

        return this.clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getUserId() {
        return this.userId;
    }

    public UserInfo getUser() {
        if (this.user == null) {
            this.user = new UserInfo();
        }

        return this.user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
        if (this.user != null) {
            this.userId = this.user.getId();
        }

    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public ConcurrentHashMap<String, Object> getGlobalVariableMap() {
        return this.globalVariableMap;
    }

    public void setGlobalVariableMap(ConcurrentHashMap<String, Object> globalVariableMap) {
        this.globalVariableMap = globalVariableMap;
    }

    public void addGlobalVariable(String key, Object value) {
        this.globalVariableMap.put(key, value);
    }

    public Object getGlobalVariable(String key) {
        return this.globalVariableMap.get(key);
    }

    public String getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public Context setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Boolean getExportFlag() {
        return this.exportFlag;
    }

    public Context setExportFlag(Boolean exportFlag) {
        this.exportFlag = exportFlag;
        return this;
    }
}
