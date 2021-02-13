package com.yufeng.service;

import com.yufeng.entity.DamageList;
import com.yufeng.entity.DamageListGoods;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 报损单Service接口
 *
 * @author Wensen Ma
 */
public interface DamageListService {

    /**
     * 根据id查询实体
     *
     * @param id
     * @return
     */
    DamageList findById(Integer id);

    /**
     * 获取当天最大报损单号
     *
     * @return
     */
    String getTodayMaxDamageNumber();

    /**
     * 添加报损单 以及所有报损单商品 以及 修改商品的成本均价
     *
     * @param damageList          报损单
     * @param DamageListGoodsList 报损单商品
     */
    void save(DamageList damageList, List<DamageListGoods> damageListGoodsList);

    /**
     * 根据条件查询报损单信息
     *
     * @param damageList
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<DamageList> list(DamageList damageList, Direction direction, String... properties);

    /**
     * 根据id删除报损单信息 包括报损单里的商品
     *
     * @param id
     */
    void delete(Integer id);

}
