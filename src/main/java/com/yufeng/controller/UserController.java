package com.yufeng.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yufeng.entity.Log;
import com.yufeng.entity.Menu;
import com.yufeng.entity.Role;
import com.yufeng.entity.User;
import com.yufeng.service.LogService;
import com.yufeng.service.MenuService;
import com.yufeng.service.RoleService;
import com.yufeng.service.UserService;
import com.yufeng.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当前登录用户控制器
 *
 * @author Wensen Ma
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @Resource
    private MenuService menuService;

    @Resource
    private LogService logService;

    /**
     * 用户登录请求
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String, Object> login(String imageCode, User user, BindingResult bindingResult, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isEmpty(imageCode)) {
            map.put("success", false);
            map.put("errorInfo", "请输入验证码！");
            return map;
        }
        if (!session.getAttribute("checkcode").equals(imageCode)) {
            map.put("success", false);
            map.put("errorInfo", "验证码输入错误！");
            return map;
        }
        if (bindingResult.hasErrors()) {
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
            return map;
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        try {
            subject.login(token); // 登录认证
            String userName = (String) SecurityUtils.getSubject().getPrincipal();
            User currentUser = userService.findByUserName(userName);
            session.setAttribute("currentUser", currentUser);
            List<Role> roleList = roleService.findByUserId(currentUser.getId());
            map.put("roleList", roleList);
            map.put("roleSize", roleList.size());
            map.put("success", true);
            logService.save(new Log(Log.LOGIN_ACTION, "用户登录")); // 写入日志
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("errorInfo", "用户名或者密码错误！");
            return map;
        }
    }

    /**
     * 保存角色信息
     *
     * @param roleId
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/saveRole")
    public Map<String, Object> saveRole(Integer roleId, HttpSession session) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Role currentRole = roleService.findById(roleId);
        session.setAttribute("currentRole", currentRole); // 保存当前角色信息
        map.put("success", true);
        return map;
    }

    /**
     * 加载当前用户信息
     *
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/loadUserInfo")
    public String loadUserInfo(HttpSession session) throws Exception {
        User currentUser = (User) session.getAttribute("currentUser");
        Role currentRole = (Role) session.getAttribute("currentRole");
        return "欢迎您：" + currentUser.getTrueName() + "&nbsp;[&nbsp;" + currentRole.getName() + "&nbsp;]";
    }

    /**
     * 加载权限菜单
     *
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/loadMenuInfo")
    public String loadMenuInfo(HttpSession session, Integer parentId) throws Exception {
        Role currentRole = (Role) session.getAttribute("currentRole");
        return getAllMenuByParentId(parentId, currentRole.getId()).toString();
    }

    /**
     * 获取所有菜单信息
     *
     * @param parentId
     * @param roleId
     * @return
     */
    private JsonArray getAllMenuByParentId(Integer parentId, Integer roleId) {
        JsonArray jsonArray = this.getMenuByParentId(parentId, roleId);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").getAsString())) {
                continue;
            } else {
                jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt(), roleId));
            }
        }
        return jsonArray;
    }

    /**
     * 根据父节点和用户角色id查询菜单
     *
     * @param parentId
     * @param roleId
     * @return
     */
    private JsonArray getMenuByParentId(Integer parentId, Integer roleId) {
        List<Menu> menuList = menuService.findByParentIdAndRoleId(parentId, roleId);
        JsonArray jsonArray = new JsonArray();
        for (Menu menu : menuList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", menu.getId()); // 节点id
            jsonObject.addProperty("text", menu.getName()); // 节点名称
            if (menu.getState() == 1) {
                jsonObject.addProperty("state", "closed"); // 根节点
            } else {
                jsonObject.addProperty("state", "open"); // 叶子节点
            }
            jsonObject.addProperty("iconCls", menu.getIcon());
            JsonObject attributeObject = new JsonObject(); // 扩展属性
            attributeObject.addProperty("url", menu.getUrl()); // 菜单请求地址
            jsonObject.add("attributes", attributeObject);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
