package com.yufeng.service;

import com.yufeng.entity.OverflowList;
import com.yufeng.entity.OverflowListGoods;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 报溢单Service接口
 *
 * @author Wensen Ma
 */
public interface OverflowListService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    OverflowList findById(Integer id);

    /**
     * 获取当天最大报溢单号
     *
     * @return
     */
    String getTodayMaxOverflowNumber();

    /**
     * 添加报溢单 以及所有报溢单商品 以及 修改商品的成本均价
     *
     * @param overflowList          报溢单
     * @param OverflowListGoodsList 报溢单商品
     */
    void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList);

    /**
     * 根据条件查询报溢单信息
     *
     * @param overflowList
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<OverflowList> list(OverflowList overflowList, Direction direction, String... properties);

    /**
     * 根据id删除报溢单信息 包括报溢单里的商品
     *
     * @param id
     */
    void delete(Integer id);

}
