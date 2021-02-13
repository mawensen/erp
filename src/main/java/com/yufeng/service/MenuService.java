package com.yufeng.service;

import com.yufeng.entity.Menu;

import java.util.List;

/**
 * 权限菜单Service实现类
 *
 * @author Wensen Ma
 */
public interface MenuService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 根据id获取权限菜单集合
     *
     * @param userId
     * @return
     */
    List<Menu> findByRoleId(int roleId);

    /**
     * 根据父节点以及角色id集合查询子节点
     *
     * @param parentId
     * @param roleId
     * @return
     */
    List<Menu> findByParentIdAndRoleId(int parentId, int roleId);

    /**
     * 根据父节点查找所有子节点
     *
     * @param parentId
     * @return
     */
    List<Menu> findByParentId(int parentId);
}
