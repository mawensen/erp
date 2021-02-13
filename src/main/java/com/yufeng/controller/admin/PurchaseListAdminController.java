package com.yufeng.controller.admin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yufeng.entity.Log;
import com.yufeng.entity.PurchaseList;
import com.yufeng.entity.PurchaseListGoods;
import com.yufeng.service.LogService;
import com.yufeng.service.PurchaseListGoodsService;
import com.yufeng.service.PurchaseListService;
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
 * 进货单Controller类
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/purchaseList")
public class PurchaseListAdminController {

    @Resource
    private PurchaseListService purchaseListService;

    @Resource
    private PurchaseListGoodsService purchaseListGoodsService;

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
     * 根据条件分页查询进货单信息
     *
     * @param purchaseList
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    @RequiresPermissions(value = {"进货单据查询"})
    public Map<String, Object> list(PurchaseList purchaseList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<PurchaseList> purchaseListList = purchaseListService.list(purchaseList, Direction.DESC, "purchaseDate");
        resultMap.put("rows", purchaseListList);
        return resultMap;
    }

    /**
     * 根据进货单id查询所有进货单商品
     *
     * @param purchaseListId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listGoods")
    @RequiresPermissions(value = {"进货单据查询"})
    public Map<String, Object> listGoods(Integer purchaseListId) throws Exception {
        if (purchaseListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        List<PurchaseListGoods> purchaseListGoodsList = purchaseListGoodsService.listByPurchaseListId(purchaseListId);
        resultMap.put("rows", purchaseListGoodsList);
        return resultMap;
    }

    /**
     * 客户统计 获取进货单的所有商品信息
     *
     * @param purchaseList
     * @param purchaseListGoods
     * @return
     * @throws Exception
     */
    @RequestMapping("/listCount")
    @RequiresPermissions(value = {"客户统计"})
    public Map<String, Object> listCount(PurchaseList purchaseList, PurchaseListGoods purchaseListGoods) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<PurchaseList> purchaseListList = purchaseListService.list(purchaseList, Direction.DESC, "purchaseDate");
        for (PurchaseList pl : purchaseListList) {
            purchaseListGoods.setPurchaseList(pl);
            List<PurchaseListGoods> plgList = purchaseListGoodsService.list(purchaseListGoods);
            for (PurchaseListGoods plg : plgList) {
                plg.setPurchaseList(null);
            }
            pl.setPurchaseListGoodsList(plgList);
        }
        resultMap.put("rows", purchaseListList);
        return resultMap;
    }

    /**
     * 获取进货单号
     *
     * @param type
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getPurchaseNumber")
    @RequiresPermissions(value = {"进货入库"})
    public String genBillCode(String type) throws Exception {
        StringBuffer biilCodeStr = new StringBuffer();
        biilCodeStr.append("JH");
        biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
        String purchaseNumber = purchaseListService.getTodayMaxPurchaseNumber(); // 获取当天最大的进货单号
        if (purchaseNumber != null) {
            biilCodeStr.append(StringUtil.formatCode(purchaseNumber));
        } else {
            biilCodeStr.append("0001");
        }
        return biilCodeStr.toString();
    }

    /**
     * 添加进货单 以及所有进货单商品 以及 修改商品的成本均价
     *
     * @param purchaseList
     * @param goodsJson
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"进货入库"})
    public Map<String, Object> save(PurchaseList purchaseList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        purchaseList.setUser(userService.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置操作用户
        Gson gson = new Gson();
        List<PurchaseListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<PurchaseListGoods>>() {
        }.getType());
        purchaseListService.save(purchaseList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "添加进货单"));
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 修改进货单的支付状态
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
        PurchaseList purchaseList = purchaseListService.findById(id);
        purchaseList.setState(1); // 修改成支付状态
        purchaseListService.update(purchaseList);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据id删除进货单信息 包括进货单里的商品
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"进货单据查询"})
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        purchaseListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "删除进货单信息" + purchaseListService.findById(id)));  // 写入日志
        resultMap.put("success", true);
        return resultMap;
    }


}
