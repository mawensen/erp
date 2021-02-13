package com.yufeng.controller.admin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yufeng.entity.CustomerReturnList;
import com.yufeng.entity.CustomerReturnListGoods;
import com.yufeng.entity.Log;
import com.yufeng.service.CustomerReturnListGoodsService;
import com.yufeng.service.CustomerReturnListService;
import com.yufeng.service.LogService;
import com.yufeng.service.UserService;
import com.yufeng.util.DateUtil;
import com.yufeng.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户退货单Controller类
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/customerReturnList")
public class CustomerReturnListAdminController {

    @Resource
    private CustomerReturnListService customerReturnListService;

    @Resource
    private CustomerReturnListGoodsService customerReturnListGoodsService;

    @Resource
    private LogService logService;

    @Resource
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值
    }

    /**
     * 根据条件分页查询客户退货单信息
     *
     * @param customerReturnList
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"客户退货查询"})
    public Map<String, Object> list(CustomerReturnList customerReturnList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<CustomerReturnList> customerReturnListList = customerReturnListService.list(customerReturnList, Direction.DESC, "customerReturnDate");
        resultMap.put("rows", customerReturnListList);
        return resultMap;
    }

    /**
     * 根据客户退货单id查询所有客户退货单商品
     *
     * @param customerReturnListId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listGoods")
    @RequiresPermissions(value = {"客户退货查询"})
    public Map<String, Object> listGoods(Integer customerReturnListId) throws Exception {
        if (customerReturnListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<CustomerReturnListGoods> customerReturnListGoodsList = customerReturnListGoodsService.listByCustomerReturnListId(customerReturnListId);
        resultMap.put("rows", customerReturnListGoodsList);
        return resultMap;
    }

    /**
     * 客户统计 获取客户退货单的所有商品信息
     *
     * @param purchaseList
     * @param purchaseListGoods
     * @return
     * @throws Exception
     */
    @RequestMapping("/listCount")
    @RequiresPermissions(value = {"客户统计"})
    public Map<String, Object> listCount(CustomerReturnList customerReturnList, CustomerReturnListGoods customerReturnListGoods) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<CustomerReturnList> customerReturnListList = customerReturnListService.list(customerReturnList, Direction.DESC, "customerReturnDate");
        for (CustomerReturnList crl : customerReturnListList) {
            customerReturnListGoods.setCustomerReturnList(crl);

            List<CustomerReturnListGoods> crlList = customerReturnListGoodsService.list(customerReturnListGoods);
            for (CustomerReturnListGoods crlg : crlList) {
                crlg.setCustomerReturnList(null);
            }
            crl.setCustomerReturnListGoodsList(crlList);
        }
        resultMap.put("rows", customerReturnListList);
        return resultMap;
    }


    /**
     * 获取客户退货单号
     *
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getCustomerReturnNumber")
    @RequiresPermissions(value = {"客户退货"})
    public String genBillCode(String type) throws Exception {
        StringBuffer biilCodeStr = new StringBuffer();
        biilCodeStr.append("XT");
        biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
        String customerReturnNumber = customerReturnListService.getTodayMaxCustomerReturnNumber(); // 获取当天最大的客户退货单号
        if (customerReturnNumber != null) {
            biilCodeStr.append(StringUtil.formatCode(customerReturnNumber));
        } else {
            biilCodeStr.append("0001");
        }
        return biilCodeStr.toString();
    }

    /**
     * 添加客户退货单 以及所有客户退货单商品
     *
     * @param customerReturnList
     * @param goodsJson
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"客户退货"})
    public Map<String, Object> save(CustomerReturnList customerReturnList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        customerReturnList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
        Gson gson = new Gson();
        List<CustomerReturnListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<CustomerReturnListGoods>>() {
        }.getType());
        customerReturnListService.save(customerReturnList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "添加客户退货单"));
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改退货单的支付状态
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions(value = {"客户统计"})
    public Map<String, Object> update(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        CustomerReturnList customerReturnList = customerReturnListService.findById(id);
        customerReturnList.setState(1); // 修改成支付状态
        customerReturnListService.update(customerReturnList);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据id删除客户退货单信息 包括客户退货单里的商品
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"客户退货查询"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        customerReturnListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "删除客户退货单信息" + customerReturnListService.findById(id)));  // 写入日志
        resultMap.put("success", true);
        return resultMap;
    }
}
