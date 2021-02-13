package com.yufeng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页或者权限url跳转控制器
 *
 * @author Wensen Ma
 */
@Controller
public class IndexController {


    /**
     * 网站根目录请求
     *
     * @return
     */
    @RequestMapping("/")
    public String root() {
        return "redirect:/login.html";
    }


}
