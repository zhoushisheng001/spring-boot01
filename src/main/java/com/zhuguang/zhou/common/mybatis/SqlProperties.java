/**
 * 
 */
package com.zhuguang.zhou.common.mybatis;

import java.util.List;

/**
 * @author liheng
 * @since 1.0
 *
 */
public class SqlProperties {
	private boolean distinct;
	private List<String> selectFields;
	private List<OrderByClause> orderByClauses;
	private List<Criteria> criterias;
	
	public boolean isDistinct() {
		return distinct;
	}
	
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	public List<String> getSelectFields() {
		return selectFields;
	}
	public void setSelectFields(List<String> selectFields) {
		this.selectFields = selectFields;
	}
	public List<OrderByClause> getOrderByClauses() {
		return orderByClauses;
	}
	public void setOrderByClauses(List<OrderByClause> orderByClauses) {
		this.orderByClauses = orderByClauses;
	}
	public List<Criteria> getCriterias() {
		return criterias;
	}
	public void setCriterias(List<Criteria> criterias) {
		this.criterias = criterias;
	}
}
