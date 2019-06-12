package com.zhuguang.zhou.common.service;


import com.zhuguang.zhou.common.mapper.AbstractTreeMapper;
import com.zhuguang.zhou.export.ApplicationException;
import com.zhuguang.zhou.pojo.GenericTreeModel;
import com.zhuguang.zhou.pojo.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <pre>
 *  树结构类型的根类
 * </pre>
 */

public class AbstractTreeService<T extends GenericTreeModel> extends GenericService<T, Long> {

    public AbstractTreeService(@Autowired AbstractTreeMapper<T> abstractTreeMapper) {
        super(abstractTreeMapper);
    }

    public AbstractTreeMapper<T> getMapper() {
        return (AbstractTreeMapper<T>) super.genericMapper;
    }

    @Override
    public int insert(T node) {
        int result = 0;
        if (node != null) {
            if (node.getParentId() == null) {
                node.setParentId(GenericTreeModel.ROOT_ID);
            }

            try {
                node = editNode(node, GenericTreeModel.ROOT_COUNT);
                result = super.insert(node);
            } catch (Exception e) {
                e.printStackTrace();
                if (logger.isErrorEnabled()) {
                    logger.error(" insert(", node.toString(), ") is error!");
                }
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(" insert(GenericTreeModel), GenericTreeModel is null!");
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int update(T... treeDomains) {
        if (treeDomains != null) {
            try {
                for (T treeDomain : treeDomains) {
                    if (treeDomain != null && treeDomain.getId() != null) {
                        T oldTreeDomain = get((Long) treeDomain.getId());

                        if (treeDomain.getParentId() == null) {
                            treeDomain.setParentId(GenericTreeModel.ROOT_ID);
                        }
                        if (oldTreeDomain.getParentId() == null) {
                            oldTreeDomain.setParentId(GenericTreeModel.ROOT_ID);
                        }

                        long oldParentId = oldTreeDomain.getParentId().longValue();
                        long currentParentId = treeDomain.getParentId().longValue();

                        boolean parentIdFlag = (oldParentId == currentParentId);
                        if (oldTreeDomain.getDisplayIndex() == null) {
                            oldTreeDomain.setDisplayIndex(49);
                        }
                        if (treeDomain.getDisplayIndex() == null) {
                            treeDomain.setDisplayIndex(50);
                        }
                        boolean displayIndexFlag = oldTreeDomain.getDisplayIndex().longValue() == treeDomain.getDisplayIndex().longValue();

                        if (parentIdFlag) {
                            if (!displayIndexFlag) {
                                updateDisplayIndex(treeDomain);
                            }
                            return super.update(treeDomain);
                        } else {
                            //resetDisplayIndex(oldTreeDomain.getCode());
                            return changeParent(oldTreeDomain, treeDomain);
                        }
                    }
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" update(", treeDomains, ") is error!");
                }
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }
        return 1;
    }

    @Override
    public int disable(Long... ids) {
        int result = 1;

        try {
            if (ids != null) {
                for (Long id : ids) {
                    if (id != null) {
                        GenericTreeModel myMenu = get(id);
                        int left = myMenu.getLeft();
                        int right = myMenu.getRight();

                        // 删除的值
                        int count = right - left + 1;

                        // 删除所有节点包括所有子节点
                        getMapper().disableNode(myMenu.getCode(), left, right);

                        // 所有节点的右值大于等于right的右值减去count
                        getMapper().updateRight(Boolean.TRUE, myMenu.getCode(), right, count);
                        // 所有节点的左值大于等于left的左值减去count
                        getMapper().updateLeft(Boolean.TRUE, myMenu.getCode(), left, count);
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" disable(ids ={", ids, "}) is error!");
            }
            result = 0;
            throw new ApplicationException(ResponseCode.DISABLE_EXCEPTION);
        }
        return result;
    }

    @Override
    public int delete(Long... ids) {
        int result = 1;

        try {
            if (ids != null) {
                for (Long id : ids) {
                    if (id != null) {
                        GenericTreeModel myMenu = get(id);
                        int left = myMenu.getLeft();
                        int right = myMenu.getRight();

                        // 删除的值
                        int count = right - left + 1;

                        // 删除所有节点包括所有子节点
                        getMapper().deleteNode(myMenu.getCode(), left, right);

                        // 所有节点的右值大于等于right的右值减去count
                        getMapper().updateRight(Boolean.TRUE, myMenu.getCode(), right, count);
                        // 所有节点的左值大于等于left的左值减去count
                        getMapper().updateLeft(Boolean.TRUE, myMenu.getCode(), left, count);
                    }
                }
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" delete(ids ={", ids, "}) is error!");
            }
            result = 0;
            throw new ApplicationException(ResponseCode.DELETE_EXCEPTION);
        }
        return result;
    }

    public List<T> getAll() {
        List<T> result = new ArrayList<T>();
        for (T node : getRoots()) {
            result.add(node);
            result.addAll(getChildrenByCode(node.getCode()));
        }
        return result;
    }

    /**
     * 获取Code(根:parent_id = 0)菜单
     *
     * @param code
     * @return
     */
    public T getRootByCode(String code) {
        T result = null;
        if (!StringUtils.isEmpty(code)) {
            try {
                result = getMapper().getRootByCode(code);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" getRootByCode(code =" + code + ") is error!");
                }
                throw new ApplicationException(ResponseCode.SELECT_EXCEPTION);
            }
        }
        return result;
    }

    /**
     * 获取Code(根:parent_id = 0)菜单
     *
     * @param code
     * @return
     */
    public T getNodeByCode(String code) {
        T result = null;
        if (!StringUtils.isEmpty(code)) {
            try {
                result = getMapper().getNodeByCode(code);
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" getNodeByCode(code =" + code + ") is error!");
                }
                throw new ApplicationException(ResponseCode.SELECT_EXCEPTION);
            }
        }
        return result;
    }

    /**
     * 获取所有根节点(根:parent_id = 0)菜单
     *
     * @return
     */
    public List<T> getRoots() {
        List<T> result = null;
        try {
            result = getMapper().getRoots();
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" getRoots() is error!");
            }
            throw new ApplicationException(ResponseCode.SELECT_EXCEPTION);
        }

        return result;
    }

    /**
     * 获取code下的所有层级的子菜单(包含level=0的菜单)
     *
     * @param code
     * @return
     */
    public List<T> getAllByCode(String code) {
        List<T> result = getChildrenByNode(getRootByCode(code));
        return result;
    }

    /**
     * 获取code下的所有层级的子菜单(不包含level=0的菜单)
     *
     * @param code
     * @return
     */
    public List<T> getChildrenByCode(String code) {
        List<T> result = getAllByCode(code);
        if (result.size() > 0) {
            result.remove(0);
        }
        return result;
    }

    /**
     * 获取完整树状数据
     * <p>
     * 根据子节点ID把所有上级数据补齐
     *
     * @param code 模块编码
     * @param ids  包含的子节点ID
     * @return
     */
    public List<T> getChildren(String code, Long[] ids) {
        List<T> result = null;

        if (ids != null && ids.length > 0) {
            result = getChildrenByCode(code);
            if (result.size() > 0) {
                StringBuffer sb = new StringBuffer(1024);
                for (Long id : ids) {
                    sb.append(",").append(id);
                }
                sb.append(",");

                List<T> list = new ArrayList<T>();

                boolean flag = true;
                while (flag) {
                    flag = false;
                    Iterator<T> it = result.iterator();
                    while (it.hasNext()) {
                        T t = it.next();
                        if (sb.indexOf("," + t.getId() + ",") != -1) {
                            list.add(t);
                            it.remove();
                            flag = true;
                        }
                    }
                }
                result.clear();
                result.addAll(list);
            }
        }

        return defaultList(result);
    }

    /**
     * 获取id下的所有层级的子菜单(包含当前id菜单)
     *
     * @param id
     * @return
     */
    public List<T> getChildren(Long id) {
        List<T> result = getChildrenByNode(get(id));
        return defaultList(result);
    }

    /**
     * 获取parentId下的所有层级的子菜单(不包含当前parentId菜单)
     *
     * @param parentId
     * @return
     */
    public List<T> getChildrenByParentId(Long parentId) {
        List<T> result = getChildren(parentId);
        if (result.size() > 0) {
            result.remove(0);
        }
        return defaultList(result);
    }

    /**
     * 获取parentId下的一级子菜单(不包含当前parentId菜单)
     *
     * @param parentId
     * @return
     */
    public List<T> getChild(Long parentId) {
        List<T> result = null;
        if (parentId == null) {
            parentId = (Long) GenericTreeModel.ROOT_ID;
        }
        try {
            result = getMapper().getChild(parentId);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" getChild(parentId =" + parentId + ") is error!");
            }
            throw new ApplicationException(ResponseCode.SELECT_EXCEPTION);
        }

        return defaultList(result);
    }

    /**
     * 对code下的所有节点，重新排序
     *
     * @param code
     */
    public void resetDisplayIndex(String code) {
        //module = StringsUtils.strip(module);
        if (!"".equals(code)) {
            try {
                //T node = getRootByCode(code);
                T node = getNodeByCode(code);
                if (node != null) {
                    resetLeftRightValue(node, 0, 0);
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" resetDisplayIndex(code =" + code + ") is error!");
                }
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }
    }

    /**
     * 更新指定节点排序值
     *
     * @param nodes
     */
    public void updateDisplayIndex(T... nodes) {
        if (nodes != null && nodes.length > 0) {
            try {
                for (T node : nodes) {
                    if (node != null) {
                        getMapper().updateDisplayIndex(node);
                    }
                }

                T parent = get((Long) get((Long) nodes[0].getId()).getParentId());
                if (parent != null) {
                    resetLeftRightValue(parent, parent.getLeft() - 1, parent.getLevel());
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" updateDisplayIndex(nodes) is error!");
                }
                throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
            }
        }
    }

    /**
     * 判断是否存在code模块
     *
     * @param code
     * @return
     */
    public boolean existsByCode(String code) {
        try {
            //return getRootByCode(code) != null;
            return getNodeByCode(code) != null;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" existsByCode(code =" + code + ") is error!");
            }
            throw new ApplicationException();
        }
    }


    /**
     * 重置该节点的左右值，他的儿子的左右值也将改变
     *
     * @param node
     * @param index
     * @param level
     * @return
     */
    private int resetLeftRightValue(T node, int index, int level) {
        try {
            index++;
            node.setLeft(index);

            node.setLevel(level);

            List<T> children = getMapper().getChild((Long) node.getId());
            if (children != null && children.size() > 0) {
                for (T child : children) {
                    index = resetLeftRightValue(child, index, level + 1);
                }
            }

            index++;
            node.setRight(index);

            getMapper().update(node);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(" resetLeftRightValue(node =" + node.getId() + ", index =" + index + ", level =" + level + ") is error!");
            }
            throw new ApplicationException(ResponseCode.UPDATE_EXCEPTION);
        }

        return index;
    }

    /**
     * 获取当前节点下的所有孩子结点。
     *
     * @param node
     * @return
     */
    public List<T> getChildrenByNode(T node) {
        List<T> result = null;

        if (node != null) {
            try {
                if (node != null) {
                    result = getMapper().getChildren(node.getCode(), node.getLeft(), node.getRight());
                }
            } catch (Exception e) {
                if (logger.isErrorEnabled()) {
                    logger.error(" getChildrenByNode(node) is error!");
                }
                throw new ApplicationException(ResponseCode.SELECT_EXCEPTION);
            }
        }

        return defaultList(result);
    }

    private List<T> defaultList(List<T> list) {
        return list;
    }


    private int editCrossNode(T node, T parentNode) {
        Integer offset = ((node.getLeft().intValue() - parentNode.getRight().intValue()) + 3);

        List<T> myCross = getMapper().getChildren(node.getCode(), parentNode.getRight() + 1, node.getLeft() - 1);
        int count = 0;
        for (T n :
                myCross) {
            n.setLeft(n.getLeft() + offset);
            n.setRight(n.getRight() + offset);

            count += super.update(n);
        }
        return count;
    }

    private T editNode(T node, int offset) {
        // 最近节点左右值，用于确认本节点的左值
        int leftRigth = 0;
        // 更新节点级别值
        int level = 0;

        // 按上级ID和显示顺序，查找上面最接近新插入节点的数据，获取其节点右值
        T nearestMenu = getMapper().getNearestNode((Long) node.getParentId(), node.getDisplayIndex());

        if (nearestMenu != null) {
            if (nearestMenu.getParentId().longValue() == node.getParentId().longValue()) {
                //最近菜单是同级
                leftRigth = nearestMenu.getRight();
                level = nearestMenu.getLevel();
            } else {
                //最近菜单是父级
                leftRigth = nearestMenu.getLeft();
                level = nearestMenu.getLevel() + 1;
            }
            node.setCode(nearestMenu.getCode());
        }else{
            if(node.getCode() == null) {
                node.setCode(String.valueOf(node.getId()));
            }
        }

        // 所有节点的右值大于等于right的加count
        getMapper().updateRight(Boolean.FALSE, node.getCode(), leftRigth, offset);
        // 所有节点的左值大于等于right的加count
        getMapper().updateLeft(Boolean.FALSE, node.getCode(), leftRigth, offset);

        // 空出的值范围，放入本节点中
        node.setLeft(leftRigth + 1);
        node.setRight(leftRigth + offset);
        node.setLevel(level);
        return node;
    }

    int changeParent(T oldNode, T curNode) {
        List<T> myChild = getMapper().getChildren(oldNode.getCode(), oldNode.getLeft(), oldNode.getRight());

        Integer offset = ((oldNode.getRight().intValue() - oldNode.getLeft().intValue()) + 1);
        curNode = editNode(curNode, offset);

        //int count =  super.update(curNode);

        if (myChild.size() > 0) {
            /*左右值偏移量*/
            Integer childOffset = (curNode.getLeft().intValue() - oldNode.getLeft().intValue());
            /*Level偏移量*/
            Integer lvlOffset = (curNode.getLevel().intValue() - oldNode.getLevel().intValue());
            int count = 0;
            for (T node : myChild) {
                if (node.getId().longValue() == curNode.getId().longValue()) {
                    node.setParentId(curNode.getParentId());
                    node.setCode(curNode.getCode());
                }
                node.setLeft(node.getLeft() + childOffset);
                node.setRight(node.getRight() + childOffset);
                node.setLevel(node.getLevel() + lvlOffset);
                count += super.update(node);
            }

            //return super.updateSelective(myChild.toArray(new Long[myChild.size()]));

            return count;
        } else {
            return super.update(curNode);
        }
    }

}