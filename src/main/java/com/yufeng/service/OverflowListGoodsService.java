package com.yufeng.service;

import com.yufeng.entity.OverflowListGoods;

import java.util.List;

/**
 * 报溢单商品Service接口
 *
 * @author Wensen Ma
 */
public interface OverflowListGoodsService {

    /**
     * 根据报溢单id查询所有报溢单商品
     *
     * @param overflowListId
     * @return
     */
    List<OverflowListGoods> listByOverflowListId(Integer overflowListId);


}
