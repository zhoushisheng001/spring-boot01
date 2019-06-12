package com.zhuguang.zhou.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 分页对象
 * @author liheng
 * @since 1.0
 */
public class Pagination<T> extends PageSize {

	private List<T> rows = null; //当前返回的记录列表
	private int rowTotal = 0; //总记录数
	private int pageTotal = 0; //总页数

	@JsonIgnore
    private boolean count = true; //是否进行总记录统计

	@JsonIgnore
    private GenericBO criteria;

	private List<Aggregation> aggregations;

    public Pagination() {
        this(1, DEFAULT_PAGESIZE,true);
    }

    public Pagination(int page, int pageSize, boolean count) {
        setPage(page);
        setPageSize(pageSize);
        this.count = count;
    }

	public static Pagination getInstance(int page, int pageSize) {
        return new Pagination(page, pageSize, true);
    }

    public static Pagination getInstance2Top(int top) {
        return new Pagination(0, top,false);
    }

    public static Pagination getInstance4BO(GenericBO genericBO) {
		Pagination pagination = new Pagination(genericBO.getPage(), genericBO.getPageSize(), true);
		pagination.setCriteria(genericBO);
    	return pagination;
    }

    public static Pagination getInstance2Top4BO(GenericBO genericBO) {
		Pagination pagination = new Pagination(0, genericBO.getPageSize(), false);
		pagination.setCriteria(genericBO);
		return pagination;
    }

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(int rowTotal) {
		this.rowTotal = rowTotal;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

    public boolean isCount() {
        return count;
    }

    public GenericBO getCriteria() {
        return criteria;
    }

    public void setCriteria(GenericBO criteria) {
        this.criteria = criteria;
    }

	public List<Aggregation> getAggregations() {
		return aggregations;
	}

	public void setAggregations(List<Aggregation> aggregations) {
		this.aggregations = aggregations;
	}
}
