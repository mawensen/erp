package com.yufeng.service.impl;

import com.yufeng.entity.RoleMenu;
import com.yufeng.repository.RoleMenuRepository;
import com.yufeng.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 角色权限菜单关联Service实现类
 *
 * @author Wensen Ma
 */
@Service("roleMenuService")
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuRepository roleMenuRepository;

    @Override
    public void deleteByRoleId(Integer roleId) {
        roleMenuRepository.deleteByRoleId(roleId);
    }

    @Override
    public void save(RoleMenu roleMenu) {
        roleMenuRepository.save(roleMenu);
    }


}
