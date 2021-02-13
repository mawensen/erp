package com.yufeng.service;

import com.yufeng.entity.DamageListGoods;

import java.util.List;

/**
 * 报损单商品Service接口
 *
 * @author Wensen Ma
 */
public interface DamageListGoodsService {

    /**
     * 根据报损单id查询所有报损单商品
     *
     * @param damageListId
     * @return
     */
    List<DamageListGoods> listByDamageListId(Integer damageListId);


}
