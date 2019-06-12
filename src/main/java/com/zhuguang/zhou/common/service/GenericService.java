package com.zhuguang.zhou.common.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.zhuguang.zhou.common.datascope.SpringUtils;
import com.zhuguang.zhou.common.mapper.GenericMapper;
import com.zhuguang.zhou.export.ApplicationException;
import com.zhuguang.zhou.pojo.BooleanFlag;
import com.zhuguang.zhou.pojo.Context;
import com.zhuguang.zhou.pojo.ContextUtils;
import com.zhuguang.zhou.pojo.GenericModel;
import com.zhuguang.zhou.pojo.GenericQuery;
import com.zhuguang.zhou.pojo.Pagination;
import com.zhuguang.zhou.pojo.ResponseCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 *
 * @param <T>
 * @param <PK>
 */
@Transactional
public abstract class GenericService<T, PK> {
    protected static final Logger logger = LoggerFactory.getLogger(GenericService.class);
    protected GenericMapper<T, PK> genericMapper;

    public GenericService(GenericMapper<T, PK> genericMapper) {
        this.genericMapper = genericMapper;
    }

    /**
     * 插入数据
     *
     * 如果主键是基于DB的方式，数据插入成功后，主键值会自动填充到输入对象中
     *
     * @param data 数据
     * @return 返回操作记录数
     */
    public int insert(T data) {
        int result = 0;
        try {
            setDefault(data, true);
            result = genericMapper.insert(data);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_EXCEPTION);
        }

        return result;
    }

    /**
     * 插入数据，忽略值为null的字段
     * @param data 数据
     * @return 返回操作记录数
     */
    public int insertSelective(T data) {
        int result = 0;
        try {
            setDefault(data, true);
            result = genericMapper.insertSelective(data);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_EXCEPTION);
        }

        return result;
    }

    /**
     * 批量插入数据
     * @param datas 数据
     * @return 返回操作记录数
     */
    public int insertBatch(List<T> datas) {
        int result = 0;
        try {
            if (datas != null) {
                for (T data : datas) {
                    setDefault(data, true);
                }
            }
            result = genericMapper.insertBatch(datas);
        } catch (Exception e) {
            logger.error(ResponseCode.INSERT_BATCH_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.INSERT_BATCH_EXCEPTION);
        }

        return result;
    }

    /**
     * 更新数据
     * 主键为更新条件，其他为数据
     * @param datas 数据
     * @return 更新结果行数
     */
    public int update(T... datas) {
        int result = 0;
        if (datas != null) {
            try {
                for (T data : datas) {
                    setDefault(data, false);
                    result += genericMapper.update(data);
                }
            } catch (Exception e) {
                logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }

        return result;
    }

    /**
     * 更新数据，忽略空字段
     * 主键为更新条件，其他非null字段为数据
     * @param datas 数据
     * @return 更新结果行数
     */
    public int updateSelective(T... datas) {
        int result = 0;
        if (datas != null) {
            try {
                for (T data : datas) {
                    setDefault(data, false);
                    result += genericMapper.updateSelective(data);
                }
            } catch (Exception e) {
                logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }

        return result;
    }

    /**
     * 通过主键删除记录
     * @param ids  主键
     * @return    删除行数
     */
    public int delete(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.delete(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.DELETE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.DELETE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键使记录无效（相当于逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    public int disable(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.disable(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.DELETE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.DELETE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键使记录有效（相当于恢复逻辑删除）
     * @param ids  主键
     * @return    更新结果行数
     */
    public int enable(PK... ids) {
        int result = 0;
        try {
            result = genericMapper.enable(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.UPDATE_EXCEPTION.getMessage(), e);
            throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
        }

        return result;
    }

    /**
     * 通过主键获取数据
     * @param id  主键
     * @return    一行数据
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public T get(PK id) {
        T result = null;
        try {
            result = genericMapper.get(id);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_ONE_EXCEPTION.getMessage(), e);
        }

        return result;
    }



    /**
     * 通过主键获取数据
     * @param ids  主键
     * @return List 如果无数据时，返回是长度为0的List对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> getByIds(PK... ids) {
        List<T> result = null;
        try {
            result = genericMapper.getByIds(ids);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_ONE_EXCEPTION.getMessage(), e);
        }
        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }

    /**
     * 通过Model获取数据
     * @param data  Model数据，非null字段都做为条件查询
     * @return List 如果无数据时，返回是长度为0的List对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<T> selectAll(T data) {
        List<T> result = null;
        try {
            result = genericMapper.selectAll(data);
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_EXCEPTION.getMessage(), e);
        }

        if (result == null) {
            result = new ArrayList<T>();
        }
        return result;
    }

    /**
     * 通过pagination对象进行相关参数查询，获取分页（或Top n）数据
     * @param pagination
     *    如果通过getInstance2Top 和 getInstance2Top4BO 方法构造的实例，是执行Top n的查询
     *    反之，是进行分页查询
     * @return  返回的结果存入pagination对象的data属性
     *          如果无数据时，返回是长度为0的List对象
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void search(Pagination<T> pagination) {
        executePagination(new PaginationCallback<T>() {
            public List<T> execute(Pagination<T> pagination) {
                return genericMapper.search(pagination);
            }
        }, pagination);
    }

    protected void executePagination(PaginationCallback<T> callback, Pagination<T> pagination) {
        try {
            if(null != pagination) {

                String elasticsearchFlag = pagination.getCriteria().getElasticsearchFlag();

                // 检查Sql注入的问题
//                GenericSearchInjectService genericSearchInjectService = (GenericSearchInjectService)SpringUtils.getBean(GenericSearchInjectService.class);
//                if(genericSearchInjectService != null && null != pagination.getCriteria()){
//                    String  menuId = pagination.getCriteria().getMenuId();
//                    String genericSearchCode = pagination.getCriteria().getGenericSearchCode();
//                    Set<String>  genericSearthColumns = genericSearchInjectService.getGenericSearchColumnByMenuIdAndCode(Long.valueOf(menuId),genericSearchCode,elasticsearchFlag);
//                    GenericQueryExample genericQueryExample = pagination.getCriteria().getGeneric();
//                    if(genericQueryExample != null && !CollectionUtils.isEmpty(genericQueryExample.getVos())){
//                        checkSqlInject(genericQueryExample.getVos(),genericSearthColumns,elasticsearchFlag);
//                    }
//                }

                // elasticsearchFlag为'Y'时从elasticsearch中获取数据，否则从数据中获取
                if(BooleanFlag.Y.getValue().equals(elasticsearchFlag)) {
                    ElasticsearchClient elasticsearchClient = (ElasticsearchClient) SpringUtils.getBean("framework_elasticsearch");
                    // elasticsearchClient为空抛出异常
                    if(null == elasticsearchClient) {
                        throw new ApplicationException(ResponseCode.UNKOWN_ELASTICSEARCH_BEAN);
                    }
                    elasticsearchClient.queryByPage(pagination);
                } else {
                    PageHelper.startPage(pagination.getPage(), pagination.getPageSize(), pagination.isCount());
                    List<T> pageResult = callback.execute(pagination);

                    if (pagination.isCount()) {
                        Page page = (Page) pageResult;

                        pagination.setRowTotal((int) page.getTotal());
                        pagination.setPageTotal(page.getPages());
                    }

                    List<T> result = new ArrayList<T>();
                    if (pageResult != null && pageResult.size() > 0) {
                        result.addAll(pageResult);
                    }

                    pagination.setRows(result);
                }
            }
        } catch (Exception e) {
            logger.error(ResponseCode.SELECT_PAGINATION_EXCEPTION.getMessage(), e);
        }
    }

    /**
     * 检查查询字段是否存在SQL注入的风险
     * @param genericQueries
     * @param genericSearthColumns
     * @param isEsFlag
     */
    private void checkSqlInject(List<GenericQuery> genericQueries, Set<String> genericSearthColumns, String isEsFlag) {
        Set<String> genericQuerySet = new HashSet<String>();
        Boolean bool = false;
        if (BooleanFlag.Y.getValue().equals(isEsFlag)) {
            bool = true;
        }
        for (GenericQuery genericQuery : genericQueries) {
            if (bool) {
                genericQuerySet.add(genericQuery.getPropertyName());
            } else {
                genericQuerySet.add(genericQuery.getColumnName());
            }
            List<String> values = genericQuery.getValues();
            if(!CollectionUtils.isEmpty(values)){
                List<String>  list = new ArrayList<String>(values.size());
                for(String str:values){
                    list.add(org.apache.commons.lang3.StringUtils.replaceAll(str,"\'","''"));
                }
                genericQuery.setValues(list);
            }
        }
        Long  size  = genericQuerySet.parallelStream().filter(key -> !genericSearthColumns.contains(key)).count();
        if(size > 0){
            throw new ApplicationException(1500,"参数不合法");
        }
    }

    /**
     * 设置添加公用参数
     *
     * @param data
     */
    private void setDefault(T data, boolean isNew) {
        if (data instanceof GenericModel) {
            GenericModel model = (GenericModel) data;
            Context context = ContextUtils.get();
            if(context != null && !StringUtils.isEmpty(context.getTraceId())){
                model.setTraceId(context.getTraceId());
            }
            if (isNew) {
                model.setCreationDate(new Timestamp(System.currentTimeMillis()));
                if (context != null) {
                    if (!StringUtils.isEmpty(context.getUserId())) {
                        model.setCreatedBy(context.getUserId());
                    }else{
                        model.setCreatedBy("System");
                    }

                }
            }
            model.setUpdationDate(new Timestamp(System.currentTimeMillis()));
            if (context != null) {
                if (!StringUtils.isEmpty(context.getUserId())) {
                    model.setUpdatedBy(context.getUserId());
                } else{
                    model.setUpdatedBy("System");
                }
            }
            if (model.getEnabledFlag() == null) {
                model.setEnabledFlag(1L);
            }
        }
    }
}