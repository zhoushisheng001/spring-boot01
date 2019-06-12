package com.zhuguang.zhou.common.mapper;

import com.zhuguang.zhou.pojo.GenericTreeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <pre>
 * 菜单数据访问接口
 * </pre>
 */
@Mapper
public interface AbstractTreeMapper<T extends GenericTreeModel> extends GenericMapper<T, Long> {
    /**
     * 根据Code获取ParentId=0 的节点
     * @param code
     * @return
     */
	T getRootByCode(String code);

    /**
     * 根据Code获取节点
     * @param code
     * @return
     */
    T getNodeByCode(String code);

    /**
     * 获取所有的根节点
     * @return
     */
	List<T> getRoots();
	
	T getNearestNode(@Param("parentId") Long parentId, @Param("displayIndex") Integer displayIndex);

    /**
     * 获取所有的儿子节点
     * @param parentId
     * @return
     */
    List<T> getChild(Long parentId);

    /**
     * 获取所有的孩子节点
     * @param code
     * @param left
     * @param right
     * @return
     */
    List<T> getChildren(@Param("code") String code, @Param("left") Integer left, @Param("right") Integer right);
    
    List<T> getPath(@Param("code") String code, @Param("left") Integer left, @Param("right") Integer right);

    /**
     * 更新排序号
     * @param treeDomain
     */
    void updateDisplayIndex(T treeDomain);

    /**
     * 更新右值
     * @param deleteFlag
     * @param code
     * @param right
     * @param count
     */
    void updateRight(@Param("deleteFlag") Boolean deleteFlag, @Param("code") String code, @Param("right") Integer right, @Param("count") Integer count);

    /**
     * 更新左值
     * @param deleteFlag
     * @param code
     * @param right
     * @param count
     */
    void updateLeft(@Param("deleteFlag") Boolean deleteFlag, @Param("code") String code, @Param("right") Integer right, @Param("count") Integer count);

    /**
     * 删除节点
     * @param code
     * @param left
     * @param right
     */
    void deleteNode(@Param("code") String code, @Param("left") Integer left, @Param("right") Integer right);

    /**
     * 禁用节点
     * @param code
     * @param left
     * @param right
     */
    void disableNode(@Param("code") String code, @Param("left") Integer left, @Param("right") Integer right);
}