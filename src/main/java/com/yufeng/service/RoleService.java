package com.yufeng.service;

import com.yufeng.entity.Role;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 角色Service实现类
 *
 * @author Wensen Ma
 */
public interface RoleService {

    /**
     * 根据用户id查询角色集合
     *
     * @param id
     * @return
     */
    List<Role> findByUserId(Integer id);

    /**
     * 根据id查询实体
     *
     * @param roleId
     * @return
     */
    Role findById(Integer id);

    /**
     * 查询所有角色信息
     *
     * @return
     */
    List<Role> listAll();

    /**
     * 根据条件分页查询角色信息
     *
     * @param user
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Role> list(Role role, Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 获取总记录数
     *
     * @param user
     * @return
     */
    Long getCount(Role role);

    /**
     * 添加或者修改角色信息
     *
     * @param role
     */
    void save(Role role);

    /**
     * 根据id删除角色
     *
     * @param id
     */
    void delete(Integer id);


}
