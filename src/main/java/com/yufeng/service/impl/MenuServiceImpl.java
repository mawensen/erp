package com.yufeng.service.impl;

import com.yufeng.entity.Menu;
import com.yufeng.repository.MenuRepository;
import com.yufeng.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限菜单Service实现类
 *
 * @author Wensen Ma
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findByRoleId(int roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    @Override
    public List<Menu> findByParentIdAndRoleId(int parentId, int roleId) {
        return menuRepository.findByParentIdAndRoleId(parentId, roleId);
    }

    @Override
    public List<Menu> findByParentId(int parentId) {
        return menuRepository.findByParentId(parentId);
    }

    @Override
    public Menu findById(Integer id) {
        return menuRepository.findOne(id);
    }

}
