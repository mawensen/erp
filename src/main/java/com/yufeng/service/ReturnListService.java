package com.yufeng.service;

import com.yufeng.entity.ReturnList;
import com.yufeng.entity.ReturnListGoods;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 退货单Service接口
 *
 * @author Wensen Ma
 */
public interface ReturnListService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    ReturnList findById(Integer id);

    /**
     * 获取当天最大退货单号
     *
     * @return
     */
    String getTodayMaxReturnNumber();

    /**
     * 添加退货单 以及所有退货单商品
     *
     * @param returnList          退货单
     * @param ReturnListGoodsList 退货单商品
     */
    void save(ReturnList returnList, List<ReturnListGoods> returnListGoodsList);

    /**
     * 根据条件查询退货单信息
     *
     * @param returnList
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<ReturnList> list(ReturnList returnList, Direction direction, String... properties);

    /**
     * 根据id删除退货单信息 包括退货单里的商品
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 更新退货单
     *
     * @param returnList
     */
    void update(ReturnList returnList);
}
