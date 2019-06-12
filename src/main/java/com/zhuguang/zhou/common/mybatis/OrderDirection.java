package com.zhuguang.zhou.common.mybatis;

/**
 * @author liheng
 * @since 1.0
 *
 */
public enum OrderDirection {
	ASC(0),
	DESC(1);
	
	private int code;
	private OrderDirection(int code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return String.valueOf(code);
	}
}
