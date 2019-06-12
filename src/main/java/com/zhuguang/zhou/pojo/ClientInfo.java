//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuguang.zhou.pojo;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String KEY_INFO = "info";
    public static final String KEY_DEVICE = "device";
    public static final String KEY_VERSION = "version";
    public static final String DEVICE_APP = "app";
    public static final String DEVICE_PC = "pc";
    public static final String DEVICE_WECHAT = "wechat";
    private String computerName;
    private String networkCard;
    private String mainBoardNo;
    private String device;
    private String version;

    public ClientInfo() {
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isApp() {
        return "app".equalsIgnoreCase(this.device);
    }

    public String getComputerName() {
        return this.computerName;
    }

    public ClientInfo setComputerName(String computerName) {
        this.computerName = computerName;
        return this;
    }

    public String getNetworkCard() {
        return this.networkCard;
    }

    public ClientInfo setNetworkCard(String networkCard) {
        this.networkCard = networkCard;
        return this;
    }

    public String getMainBoardNo() {
        return this.mainBoardNo;
    }

    public ClientInfo setMainBoardNo(String mainBoardNo) {
        this.mainBoardNo = mainBoardNo;
        return this;
    }
}
