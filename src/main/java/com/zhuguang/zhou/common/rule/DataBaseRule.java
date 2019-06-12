package com.zhuguang.zhou.common.rule;

import java.io.Serializable;
import java.util.List;

public class DataBaseRule implements Serializable {
    /**
     * 数据库
     */
    private String databasename;
    /**
     * 分库的表
     */
    private List<TableRule> tables;

    public String getDatabasename() {
        return databasename;
    }

    public DataBaseRule setDatabasename(String databasename) {
        this.databasename = databasename;
        return this;
    }

    public List<TableRule> getTables() {
        return tables;
    }

    public DataBaseRule setTables(List<TableRule> tables) {
        this.tables = tables;
        return this;
    }
}
