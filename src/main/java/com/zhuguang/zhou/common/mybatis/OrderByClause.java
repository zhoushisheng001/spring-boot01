package com.zhuguang.zhou.common.mybatis;

/**
 * @author liheng
 * @since 1.0
 *
 */
public class OrderByClause {
	private String filed;
	private OrderDirection orderDirection;
	
	public OrderByClause(String filed, OrderDirection orderDirection) {
		this.filed = filed;
		this.orderDirection = orderDirection;
	}
	
	public String getFiled() {
		return filed;
	}
	public void setFiled(String filed) {
		this.filed = filed;
	}
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}
}
