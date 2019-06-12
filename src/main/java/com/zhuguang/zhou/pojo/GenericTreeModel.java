package com.zhuguang.zhou.pojo;

import java.io.Serializable;

/**
 * 通用树结构实体对象
 * @author liheng
 * @since 1.0
 */
public abstract class GenericTreeModel extends GenericModel<Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final Long ROOT_ID = 0L;
    /*左右值偏移量，默认加2*/
	public static final Integer ROOT_COUNT = 2;
    public static final String ROOT_MODULE = "ROOT";
    public final static Long DEFAULT_DISPLAY_INDEX = 50L;

	/**
	 * 字段名称：模块编码(唯一)
	 *
	 * 数据库字段信息:code
	 */
	private String code;

    /**
     * 字段名称：上级菜单
     * 
     * 数据库字段信息:parent_id
     */
    private Long parentId;


    /**
     * 字段名称：显示顺序
     * 
     * 数据库字段信息:display_index INT(10)
     */
    private Integer displayIndex;
    
    /**
     * 字段名称：节点左值
     * 
     * 数据库字段信息:left_value INT(10)
     */
    private Integer left;
    
    /**
     * 字段名称：节点右值
     * 
     * 数据库字段信息:right_value INT(10)
     */
    private Integer right;
    
    /**
     * 字段名称：节点层级
     * 
     * 数据库字段信息:lvl INT(10)
     */
    private Integer level;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getDisplayIndex() {
		return displayIndex;
	}

	public void setDisplayIndex(Integer displayIndex) {
		this.displayIndex = displayIndex;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getRight() {
		return right;
	}

	public void setRight(Integer right) {
		this.right = right;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}