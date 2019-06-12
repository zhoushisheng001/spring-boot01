package com.zhuguang.zhou.common.mybatis;

/**
 * @author liheng
 * @since 1.0
 *
 */
public enum CriteriaCondition {
	IsNull(0),
	IsNotNull(1),
	EqualTo(2),
	NotEqualTo(3),
	GreaterThan(4),
	GreaterThanOrEqualTo(5),
	LessThan(6),
	LessThanOrEqualTo(7),
	Like(8),
	NotLike(9),
	In(10),
	NotIn(11),
	Between(12),
	NotBetween(13);
	
	private int code;
	private CriteriaCondition(int code) {
		this.code = code;
	}
	
	@Override
	public String toString() {



		return String.valueOf(code);
	}
}
