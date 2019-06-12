package com.zhuguang.zhou.pojo;

/**
 * Created by lnx on 2018/10/27
 */
public class Aggregation{

    /**度量聚合STAT*/
    public static String AGGRS_TYPE_METRICS_STAT = "stat";
    /**度量聚合SUM*/
    public static String AGGRS_TYPE_METRICS_SUM = "sum";
    /**度量聚合AVG*/
    public static String AGGRS_TYPE_METRICS_AVG = "avg";
    /**度量聚合MAX*/
    public static String AGGRS_TYPE_METRICS_MAX = "max";
    /**度量聚合MIN*/
    public static String AGGRS_TYPE_METRICS_MIN = "min";
    /**度量聚合COUNT*/
    public static String AGGRS_TYPE_METRICS_COUNT = "count";
    /**桶聚合TERMS*/
    public static String AGGRS_TYPE_BUCKET_TERMS = "terms";

    /**度量聚合操作，必填，如:sum,avg,min,max,count等*/
    private String operation;

    /**度量聚合操作字段，必填*/
    private String field;

    /**度量聚合请求标识，必填，任意填写*/
    private String resultKey;

    /**
     * 度量操作结果
     * 以下是不同聚合操作对应的返回
     * sum->Double
     * avg->Double
     * min->Double
     * max->Double
     * count->Long
     */
    private Object resultVal;

    public Aggregation() {}

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public Object getResultVal() {
        return resultVal;
    }

    public void setResultVal(Object resultVal) {
        this.resultVal = resultVal;
    }
}
