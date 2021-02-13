package com.yufeng.service;


import com.yufeng.entity.UserRole;

/**
 * 用户角色关联Service接口
 *
 * @author Wensen Ma
 */
public interface UserRoleService {

    /**
     * 添加或者修改用户角色关联
     *
     * @param userRole
     */
    void save(UserRole userRole);

    /**
     * 删除用户角色关联实体
     *
     * @param id
     */
    void delete(UserRole userRole);


    /**
     * 根据ID查询用户角色关联实体
     *
     * @param id
     * @return
     */
    UserRole findById(Integer id);

    /**
     * 根据用户id删除所有关联信息
     *
     * @param id
     */
    void deleteByUserId(Integer userId);

    /**
     * 根据角色id删除所有关联信息
     *
     * @param id
     */
    void deleteByRoleId(Integer userId);
}
