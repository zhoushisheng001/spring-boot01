package com.zhuguang.zhou.common.service;



import com.zhuguang.zhou.pojo.Pagination;

import java.util.List;

/**
 * Created by henry on 2017/1/31.
 */
public interface PaginationCallback<T> {
    public List<T> execute(Pagination<T> pagination);
}
