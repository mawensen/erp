package com.yufeng.service;

import com.yufeng.entity.PurchaseListGoods;

import java.util.List;

/**
 * 进货单商品Service接口
 *
 * @author Wensen Ma
 */
public interface PurchaseListGoodsService {

    /**
     * 根据进货单id查询所有进货单商品
     *
     * @param purchaseListId
     * @return
     */
    List<PurchaseListGoods> listByPurchaseListId(Integer purchaseListId);

    /**
     * 根据条件查询进货单商品
     *
     * @param PurchaseListGoods
     * @return
     */
    List<PurchaseListGoods> list(PurchaseListGoods purchaseListGoods);


}
