package com.yufeng.service;

import com.yufeng.entity.SaleList;
import com.yufeng.entity.SaleListGoods;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 销售单Service接口
 *
 * @author Wensen Ma
 */
public interface SaleListService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    SaleList findById(Integer id);

    /**
     * 获取当天最大销售单号
     *
     * @return
     */
    String getTodayMaxSaleNumber();

    /**
     * 添加销售单 以及所有销售单商品 以及 修改商品的成本均价
     *
     * @param saleList          销售单
     * @param SaleListGoodsList 销售单商品
     */
    void save(SaleList saleList, List<SaleListGoods> saleListGoodsList);

    /**
     * 根据条件查询销售单信息
     *
     * @param saleList
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<SaleList> list(SaleList saleList, Direction direction, String... properties);

    /**
     * 根据id删除销售单信息 包括销售单里的商品
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 更新销售单
     *
     * @param saleList
     */
    void update(SaleList saleList);

    /**
     * 按天统计某个日期范围内的销售信息
     *
     * @param begin
     * @param end
     * @return
     */
    List<Object> countSaleByDay(String begin, String end);

    /**
     * 按月统计某个日期范围内的销售信息
     *
     * @param begin
     * @param end
     * @return
     */
    List<Object> countSaleByMonth(String begin, String end);

}
