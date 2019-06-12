package com.zhuguang.zhou.common.mybatis;

/**
 * @author liheng
 * @since 1.0
 */
public class CommonTableColumn {
	/** 数据库字段名 */
	private String column;
	/** 对应该类字段 */
	private String property;
	/** 数据库类型  */
	private String jdbcType;
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getJdbcType() {
		return jdbcType;
	}
	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}
	
}
