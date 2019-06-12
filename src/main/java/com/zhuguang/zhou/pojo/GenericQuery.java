package com.zhuguang.zhou.pojo;

import java.util.List;

/**
 * 通用查询对象
 */
public class GenericQuery {

    /**
     * 数据表列名
     */
    private  String columnName;
    /**
     * 连接符
     */
    private  String operation;
    /**
     * 类型：日期  数值   字符 枚举
     */
    private String type;

    private List<String> values;
    /**
     * 条件之前的关系
     */
    private  String  conditionOperation;
    /**
     * 前置括号
     */
    private String  frontBrackets;
    /**
     * 属性名
     */
    private String  propertyName;
    /**
     * 后置括号
     */
    private String  postBrackets;
    /**
     * 加密策略
     */
    private String  sercurity;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getConditionOperation() {
        return conditionOperation;
    }

    public void setConditionOperation(String conditionOperation) {
        this.conditionOperation = conditionOperation;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrontBrackets() {
        return frontBrackets;
    }

    public void setFrontBrackets(String frontBrackets) {
        this.frontBrackets = frontBrackets;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPostBrackets() {
        return postBrackets;
    }

    public void setPostBrackets(String postBrackets) {
        this.postBrackets = postBrackets;
    }

    public String getSercurity() {
        return sercurity;
    }

    public void setSercurity(String sercurity) {
        this.sercurity = sercurity;
    }
}
