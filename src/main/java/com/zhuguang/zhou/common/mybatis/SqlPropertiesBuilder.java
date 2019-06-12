package com.zhuguang.zhou.common.mybatis;

import java.util.ArrayList;

/**
 * @author liheng
 * @since 1.0
 *
 */
public class SqlPropertiesBuilder {
	private SqlProperties sqlProperties;
	
	public SqlPropertiesBuilder() {
		sqlProperties = new SqlProperties();
		sqlProperties.setSelectFields(new ArrayList<String>());
		sqlProperties.setCriterias(new ArrayList<Criteria>());
		sqlProperties.setOrderByClauses(new ArrayList<OrderByClause>());
	}
	
	public SqlPropertiesBuilder addSelectFiled(String filed) {
		sqlProperties.getSelectFields().add(filed);
		return this;
	}

	public SqlPropertiesBuilder addOrderClause(String filed) {
		sqlProperties.getOrderByClauses().add(new OrderByClause(filed, OrderDirection.ASC));
		return this;
	}
	
	public SqlPropertiesBuilder addOrderClause(String filed, OrderDirection orderDirection) {
		sqlProperties.getOrderByClauses().add(new OrderByClause(filed, orderDirection));
		return this;
	}

	
	public SqlPropertiesBuilder addCriteria(String filed, CriteriaCondition condition, Object value1, Object value2) {
		sqlProperties.getCriterias().add(new Criteria(filed, condition, value1, value2));
		return this;
	}
	
	public SqlProperties build() {
		return sqlProperties;
	}
}
