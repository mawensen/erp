package com.yufeng.service;

import com.yufeng.entity.Goods;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 商品Service接口
 *
 * @author Wensen Ma
 */
public interface GoodsService {


    /**
     * 根据id查询商品实体
     *
     * @param id
     * @return
     */
    Goods findById(Integer id);

    /**
     * 修改或者修改商品信息
     *
     * @param goods
     */
    void save(Goods goods);

    /**
     * 根据条件分页查询商品信息
     *
     * @param goods
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Goods> list(Goods goods, Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 根据商品编码或者名称分页查询没有库存的商品信息
     *
     * @param codeOrName
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Goods> listNoInventoryQuantityByCodeOrName(String codeOrName, Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 分页查询有库存的商品信息
     *
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<Goods> listHasInventoryQuantity(Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 获取总记录数
     *
     * @param goods
     * @return
     */
    Long getCount(Goods goods);

    /**
     * 根据商品编码或者名称查询没有库存的商品信息信息总记录数
     *
     * @param codeOrName
     * @return
     */
    Long getCountNoInventoryQuantityByCodeOrName(String codeOrName);

    /**
     * 查询有库存的商品信息信息总记录数
     *
     * @param codeOrName
     * @return
     */
    Long getCountHasInventoryQuantity();

    /**
     * 根据id删除商品
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 获取最大的商品编号
     *
     * @return
     */
    String getMaxGoodsCode();

    /**
     * 查询库存报警商品 库存小于库存下限的商品
     *
     * @return
     */
    List<Goods> listAlarm();


}
