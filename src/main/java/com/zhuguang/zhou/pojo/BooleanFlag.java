//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuguang.zhou.pojo;

public enum BooleanFlag {
    Y("Y", "是"),
    N("N", "否"),
    TRUE("true", "是"),
    FALSE("false", "否");

    private String value;
    private String name;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private BooleanFlag(String value, String name) {
        this.setValue(value);
        this.setName(name);
    }
}
