package com.yufeng.controller.admin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yufeng.entity.Log;
import com.yufeng.entity.ReturnList;
import com.yufeng.entity.ReturnListGoods;
import com.yufeng.service.LogService;
import com.yufeng.service.ReturnListGoodsService;
import com.yufeng.service.ReturnListService;
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
 * 退货单Controller类
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/returnList")
public class ReturnListAdminController {

    @Resource
    private ReturnListService returnListService;

    @Resource
    private ReturnListGoodsService returnListGoodsService;

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
     * 根据条件分页查询退货单信息
     *
     * @param returnList
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"退货单据查询"})
    public Map<String, Object> list(ReturnList returnList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<ReturnList> returnListList = returnListService.list(returnList, Direction.DESC, "returnDate");
        resultMap.put("rows", returnListList);
        return resultMap;
    }

    /**
     * 根据退货单id查询所有退货单商品
     *
     * @param returnListId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listGoods")
    @RequiresPermissions(value = {"退货单据查询"})
    public Map<String, Object> listGoods(Integer returnListId) throws Exception {
        if (returnListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<ReturnListGoods> returnListGoodsList = returnListGoodsService.listByReturnListId(returnListId);
        resultMap.put("rows", returnListGoodsList);
        return resultMap;
    }

    /**
     * 客户统计 获取退货单的所有商品信息
     *
     * @param purchaseList
     * @param purchaseListGoods
     * @return
     * @throws Exception
     */
    @RequestMapping("/listCount")
    @RequiresPermissions(value = {"客户统计"})
    public Map<String, Object> listCount(ReturnList returnList, ReturnListGoods returnListGoods) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<ReturnList> returnListList = returnListService.list(returnList, Direction.DESC, "returnDate");
        for (ReturnList pl : returnListList) {
            returnListGoods.setReturnList(pl);
            List<ReturnListGoods> rlgList = returnListGoodsService.list(returnListGoods);
            for (ReturnListGoods rlg : rlgList) {
                rlg.setReturnList(null);
            }
            pl.setReturnListGoodsList(rlgList);
        }
        resultMap.put("rows", returnListList);
        return resultMap;
    }

    /**
     * 获取退货单号
     *
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getReturnNumber")
    @RequiresPermissions(value = {"退货出库"})
    public String genBillCode(String type) throws Exception {
        StringBuffer biilCodeStr = new StringBuffer();
        biilCodeStr.append("TH");
        biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
        String returnNumber = returnListService.getTodayMaxReturnNumber(); // 获取当天最大的退货单号
        if (returnNumber != null) {
            biilCodeStr.append(StringUtil.formatCode(returnNumber));
        } else {
            biilCodeStr.append("0001");
        }
        return biilCodeStr.toString();
    }

    /**
     * 添加退货单 以及所有退货单商品 以及 修改商品的成本均价
     *
     * @param returnList
     * @param goodsJson
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"退货出库"})
    public Map<String, Object> save(ReturnList returnList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        returnList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
        Gson gson = new Gson();
        List<ReturnListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<ReturnListGoods>>() {
        }.getType());
        returnListService.save(returnList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "添加退货单"));
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
    @RequiresPermissions(value = {"供应商统计"})
    public Map<String, Object> update(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        ReturnList returnList = returnListService.findById(id);
        returnList.setState(1); // 修改成支付状态
        returnListService.update(returnList);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据id删除退货单信息 包括退货单里的商品
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"退货单据查询"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        returnListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "删除退货单信息" + returnListService.findById(id)));  // 写入日志
        resultMap.put("success", true);
        return resultMap;
    }
}
