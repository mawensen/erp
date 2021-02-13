package com.yufeng.service;

import com.yufeng.entity.CustomerReturnListGoods;

import java.util.List;

/**
 * 客户退货单商品Service接口
 *
 * @author Wensen Ma
 */
public interface CustomerReturnListGoodsService {

    /**
     * 根据客户退货单id查询所有客户退货单商品
     *
     * @param customerReturnListId
     * @return
     */
	List<CustomerReturnListGoods> listByCustomerReturnListId(Integer customerReturnListId);

    /**
     * 统计某个商品的客户退货总量
     *
     * @param goodsId
     * @return
     */
	Integer getTotalByGoodsId(Integer goodsId);

    /**
     * 根据条件查询客户退货单商品
     *
     * @param customerReturnListGoods
     * @return
     */
	List<CustomerReturnListGoods> list(CustomerReturnListGoods customerReturnListGoods);


}
