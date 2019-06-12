package com.zhuguang.zhou.common.rule;

import java.io.Serializable;

public class TableRule  implements Serializable{
    /**
     * 表名
     */
    private String name;
    /**
     * 分片列
     */
    private String column;

    public String getName() {
        return name;
    }

    public TableRule setName(String name) {
        this.name = name;
        return this;
    }

    public String getColumn() {
        return column;
    }

    public TableRule setColumn(String column) {
        this.column = column;
        return this;
    }
}
