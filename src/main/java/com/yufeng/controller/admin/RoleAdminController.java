package com.yufeng.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yufeng.entity.Log;
import com.yufeng.entity.Menu;
import com.yufeng.entity.Role;
import com.yufeng.entity.RoleMenu;
import com.yufeng.service.*;
import com.yufeng.util.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 后台管理角色Controller
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/role")
public class RoleAdminController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private LogService logService;

    /**
     * 查询所有角色
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAll")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> listAll() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", roleService.listAll());
        logService.save(new Log(Log.SEARCH_ACTION, "查询所有角色信息")); // 写入日志
        return resultMap;
    }

    /**
     * 分页查询角色信息
     *
     * @param user
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> list(Role role, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) throws Exception {
        List<Role> roleList = roleService.list(role, page, rows, Direction.ASC, "id");
        Long total = roleService.getCount(role);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", roleList);
        resultMap.put("total", total);
        logService.save(new Log(Log.SEARCH_ACTION, "查询角色信息")); // 写入日志
        return resultMap;
    }

    /**
     * 添加或者修改角色信息
     *
     * @param role
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> save(Role role) throws Exception {
        if (role.getId() != null) { // 写入日志
            logService.save(new Log(Log.UPDATE_ACTION, "更新角色信息" + role));
        } else {
            logService.save(new Log(Log.ADD_ACTION, "添加角色信息" + role));
        }
        Map<String, Object> resultMap = new HashMap<>();
        roleService.save(role);
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 删除角色信息
     *
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> delete(Integer id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "删除角色信息" + roleService.findById(id)));  // 写入日志
        Map<String, Object> resultMap = new HashMap<>();
        userRoleService.deleteByRoleId(id); // 删除用户角色关联信息
        roleService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据父节点获取所有复选框权限菜单树
     *
     * @param parentId
     * @param roleId
     * @return
     * @throws Exception
     */
    @PostMapping("/loadCheckMenuInfo")
    @RequiresPermissions(value = {"角色管理"})
    public String loadCheckMenuInfo(Integer parentId, Integer roleId) throws Exception {
        List<Menu> menuList = menuService.findByRoleId(roleId); // 根据角色查询所有权限菜单信息
        List<Integer> menuIdList = new LinkedList<Integer>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return getAllCheckedMenuByParentId(parentId, menuIdList).toString();
    }

    /**
     * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    private JsonArray getAllCheckedMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        JsonArray jsonArray = this.getCheckedMenuByParentId(parentId, menuIdList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").getAsString())) {
                continue;
            } else {
                jsonObject.add("children", getAllCheckedMenuByParentId(jsonObject.get("id").getAsInt(), menuIdList));
            }
        }
        return jsonArray;
    }

    /**
     * 根据父节点ID和权限菜单ID集合获取复选框菜单节点
     *
     * @param parentId
     * @param menuIdList
     * @return
     */
    private JsonArray getCheckedMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        List<Menu> menuList = menuService.findByParentId(parentId);
        JsonArray jsonArray = new JsonArray();
        for (Menu menu : menuList) {
            JsonObject jsonObject = new JsonObject();
            int menuId = menu.getId();
            jsonObject.addProperty("id", menuId); // 节点id
            jsonObject.addProperty("text", menu.getName()); // 节点名称
            if (menu.getState() == 1) {
                jsonObject.addProperty("state", "closed"); // 根节点
            } else {
                jsonObject.addProperty("state", "open"); // 叶子节点
            }
            if (menuIdList.contains(menuId)) {
                jsonObject.addProperty("checked", true);
            }
            jsonObject.addProperty("iconCls", menu.getIcon());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 保存角色权限设置
     *
     * @param menuIds
     * @param roleId
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveMenuSet")
    @RequiresPermissions(value = {"角色管理"})
    public Map<String, Object> saveMenuSet(String menuIds, Integer roleId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        roleMenuService.deleteByRoleId(roleId); // 根据角色id删除所有角色权限关联实体
        if (StringUtil.isNotEmpty(menuIds)) {
            String[] idsStr = menuIds.split(",");
            for (int i = 0; i < idsStr.length; i++) { // 然后添加所有角色权限关联实体
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRole(roleService.findById(roleId));
                roleMenu.setMenu(menuService.findById(Integer.parseInt(idsStr[i])));
                roleMenuService.save(roleMenu);
            }
        }
        resultMap.put("success", true);
        logService.save(new Log(Log.ADD_ACTION, "保存角色权限设置"));  // 写入日志
        return resultMap;
    }
}
