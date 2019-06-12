package com.zhuguang.zhou.common.mybatis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liheng
 * @since 1.0
 */
public class CommonTable {
	/** 数据库表名 */
	private String table;
	/** 表对应类 */
	private String classType;
	/** 主键 */
	private CommonTableColumn idColumn;
	/** 除主键外的其他字段 */
	private List<CommonTableColumn> columnList;
	
	private String baseResultMap;

	private KeyGenMode keyGenMode;

	private String baseColumnsId;
	private String baseColumns;

	private String namespace;



	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public List<CommonTableColumn> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<CommonTableColumn> columnList) {
		this.columnList = columnList;
	}
	public KeyGenMode getKeyGenMode() {
		return keyGenMode;
	}
	public void setKeyGenMode(KeyGenMode keyGenMode) {
		this.keyGenMode = keyGenMode;
	}

    public String getBaseColumnsId() {
        return baseColumnsId;
    }

    public void setBaseColumnsId(String baseColumnsId) {
        this.baseColumnsId = baseColumnsId;
    }

    public String getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(String baseColumns) {
        this.baseColumns = baseColumns;
    }

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the idColumn
	 */
	public CommonTableColumn getIdColumn() {
		return idColumn;
	}
	/**
	 * @param idColumn the idColumn to set
	 */
	public void setIdColumn(CommonTableColumn idColumn) {
		this.idColumn = idColumn;
	}
	/**
	 * @return the baseResultMap
	 */
	public String getBaseResultMap() {
		return baseResultMap;
	}
	/**
	 * @param baseResultMap the baseResultMap to set
	 */
	public void setBaseResultMap(String baseResultMap) {
		this.baseResultMap = baseResultMap;
	}
	
	public List<CommonTableColumn> getAllColumnList() {
		List<CommonTableColumn> allColumnList = new ArrayList<CommonTableColumn>();
		allColumnList.add(idColumn);
		allColumnList.addAll(columnList);
		
		return allColumnList;
	} 
}
