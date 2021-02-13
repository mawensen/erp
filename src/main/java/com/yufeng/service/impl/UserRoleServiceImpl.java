package com.yufeng.service.impl;


import com.yufeng.entity.UserRole;
import com.yufeng.repository.UserRoleRepository;
import com.yufeng.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户角色关联Service实现类
 *
 * @author Wensen Ma
 */
@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleRepository userRoleRepository;

    @Override
    public void save(UserRole userRole) {
        userRoleRepository.save(userRole);
    }


    @Override
    public void delete(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    @Override
    public UserRole findById(Integer id) {
        return userRoleRepository.findOne(id);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        userRoleRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByRoleId(Integer userId) {
        userRoleRepository.deleteByRoleId(userId);
    }


}
