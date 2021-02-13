package com.yufeng.service;

import com.yufeng.entity.GoodsUnit;

import java.util.List;

/**
 * 商品单位Service接口
 *
 * @author Wensen Ma
 */
public interface GoodsUnitService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    GoodsUnit findById(Integer id);

    /**
     * 查询所有商品单位信息
     *
     * @return
     */
    List<GoodsUnit> listAll();

    /**
     * 修改或者修改商品单位信息
     *
     * @param goods
     */
    void save(GoodsUnit goodsUnit);

    /**
     * 根据id删除商品单位
     *
     * @param id
     */
    void delete(Integer id);
}
