package com.yufeng.controller.admin;

import com.yufeng.entity.Customer;
import com.yufeng.entity.Log;
import com.yufeng.service.CustomerService;
import com.yufeng.service.LogService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理客户Controller
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/customer")
public class CustomerAdminController {

    @Resource
    private CustomerService customerService;

    @Resource
    private LogService logService;

    /**
     * 分页查询客户信息
     *
     * @param customer
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"客户管理"})
    public Map<String, Object> list(Customer customer, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "rows", required = false) Integer rows) throws Exception {
        List<Customer> customerList = customerService.list(customer, page, rows, Direction.ASC, "id");
        Long total = customerService.getCount(customer);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", customerList);
        resultMap.put("total", total);
        logService.save(new Log(Log.SEARCH_ACTION, "查询客户信息")); // 写入日志
        return resultMap;
    }

    /**
     * 下拉框模糊查询
     *
     * @param q
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/comboList")
    @RequiresPermissions(value = {"销售出库", "客户退货", "销售单据查询", "客户退货查询"}, logical = Logical.OR)
    public List<Customer> comboList(String q) throws Exception {
        if (q == null) {
            q = "";
        }
        return customerService.findByName("%" + q + "%");
    }


    /**
     * 添加或者修改客户信息
     *
     * @param customer
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"客户管理"})
    public Map<String, Object> save(Customer customer) throws Exception {
        if (customer.getId() != null) { // 写入日志
            logService.save(new Log(Log.UPDATE_ACTION, "更新客户信息" + customer));
        } else {
            logService.save(new Log(Log.ADD_ACTION, "添加客户信息" + customer));
        }
        Map<String, Object> resultMap = new HashMap<>();
        customerService.save(customer);
        resultMap.put("success", true);
        return resultMap;
    }


    /**
     * 删除客户信息
     *
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"客户管理"})
    public Map<String, Object> delete(String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            int id = Integer.parseInt(idsStr[i]);
            logService.save(new Log(Log.DELETE_ACTION, "删除客户信息" + customerService.findById(id)));  // 写入日志
            customerService.delete(id);
        }
        resultMap.put("success", true);
        return resultMap;
    }

}
