package com.zhuguang.zhou.common.mybatis;

/**
 * @author liheng
 * @since 1.0
 *
 */
public class Criteria {
	private String filed;
	private CriteriaCondition condition;
	private Object value1;
	private Object value2;
	
	public Criteria (String filed, CriteriaCondition condition, Object value1, Object value2) {
		this.filed = filed;
		this.condition = condition;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public String getFiled() {
		return filed;
	}
	public void setFiled(String filed) {
		this.filed = filed;
	}
	public CriteriaCondition getCondition() {
		return condition;
	}
	public void setCondition(CriteriaCondition condition) {
		this.condition = condition;
	}
	public Object getValue1() {
		return value1;
	}
	public void setValue1(Object value1) {
		this.value1 = value1;
	}
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}
}
