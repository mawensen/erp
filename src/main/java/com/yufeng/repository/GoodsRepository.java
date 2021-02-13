package com.yufeng.repository;


import com.yufeng.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * 商品Repository接口
 *
 * @author Wensen Ma
 */
public interface GoodsRepository extends JpaRepository<Goods, Integer>, JpaSpecificationExecutor<Goods> {

    /**
     * 获取最大的商品编号
     *
     * @return
     */
    @Query(value = "select max(code) from t_goods", nativeQuery = true)
    String getMaxGoodsCode();

    /**
     * 查询库存报警商品 库存小于库存下限的商品
     *
     * @return
     */
    @Query(value = "SELECT * FROM t_goods WHERE inventory_quantity<min_num", nativeQuery = true)
    List<Goods> listAlarm();

}
