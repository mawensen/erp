package com.yufeng.controller.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yufeng.entity.GoodsType;
import com.yufeng.entity.Log;
import com.yufeng.service.GoodsTypeService;
import com.yufeng.service.LogService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台管理商品类别Controller
 *
 * @author Wensen Ma
 */
@RestController
@RequestMapping("/admin/goodsType")
public class GoodsTypeAdminController {

    @Resource
    private GoodsTypeService goodsTypeService;

    @Resource
    private LogService logService;

    /**
     * 加载商品类别树菜单
     *
     * @return
     * @throws Exception
     */
    @PostMapping("/loadTreeInfo")
    @RequiresPermissions(value = {"商品管理", "进货入库", "当前库存查询"}, logical = Logical.OR)
    public String loadTreeInfo() throws Exception {
        logService.save(new Log(Log.SEARCH_ACTION, "查询商品类别信息")); // 写入日志
        return getAllByParentId(-1).toString();
    }

    /**
     * 添加商品类别
     *
     * @param name
     * @param parentId
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = {"商品管理", "进货入库"}, logical = Logical.OR)
    public Map<String, Object> save(String name, Integer parentId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        GoodsType goodsType = new GoodsType();
        goodsType.setName(name);
        goodsType.setpId(parentId);
        goodsType.setIcon("icon-folder");
        goodsType.setState(0);
        logService.save(new Log(Log.ADD_ACTION, "添加商品类别信息" + goodsType));
        goodsTypeService.save(goodsType); // 保存商品类别

        GoodsType parentGoodsType = goodsTypeService.findById(parentId); // 查找父节点
        parentGoodsType.setState(1); // 修改state 1 根节点
        goodsTypeService.save(parentGoodsType); // 保存父节点商品类别


        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 商品类别删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = {"商品管理", "进货入库"}, logical = Logical.OR)
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        GoodsType goodsType = goodsTypeService.findById(id);
        if (goodsTypeService.findByParentId(goodsType.getpId()).size() == 1) { // 假如父节点下只有当前这个子节点，修改下 父节点的state状态
            GoodsType parentGoodsType = goodsTypeService.findById(goodsType.getpId());
            parentGoodsType.setState(0); // 修改state 0  叶子节点
            goodsTypeService.save(parentGoodsType); // 保存父节点商品类别
        }
        logService.save(new Log(Log.DELETE_ACTION, "删除商品类别信息" + goodsType));  // 写入日志
        goodsTypeService.delete(id); // 删除
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 根据父节点递归获取所有商品类别信息
     *
     * @param parentId
     * @return
     */
    public JsonArray getAllByParentId(Integer parentId) {
        JsonArray jsonArray = this.getByParentId(parentId);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").getAsString())) {
                continue;
            } else {
                jsonObject.add("children", getAllByParentId(jsonObject.get("id").getAsInt()));
            }
        }
        return jsonArray;
    }

    /**
     * 根据父节点查询子节点
     *
     * @param parentId
     * @return
     */
    private JsonArray getByParentId(Integer parentId) {
        JsonArray jsonArray = new JsonArray();
        List<GoodsType> goodsTypeList = goodsTypeService.findByParentId(parentId);
        for (GoodsType goodsType : goodsTypeList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", goodsType.getId()); // 节点id
            jsonObject.addProperty("text", goodsType.getName()); // 节点名称
            if (goodsType.getState() == 1) {
                jsonObject.addProperty("state", "closed"); // 根节点
            } else {
                jsonObject.addProperty("state", "open"); // 叶子节点
            }
            jsonObject.addProperty("iconCls", goodsType.getIcon());
            JsonObject attributeObject = new JsonObject(); // 扩展属性
            attributeObject.addProperty("state", goodsType.getState()); // 节点状态
            jsonObject.add("attributes", attributeObject);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


}
