package com.yufeng.repository;

import com.yufeng.entity.OverflowListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 报溢单商品Repository接口
 *
 * @author Wensen Ma
 */
public interface OverflowListGoodsRepository extends JpaRepository<OverflowListGoods, Integer>, JpaSpecificationExecutor<OverflowListGoods> {

    /**
     * 根据报溢单id查询所有报溢单商品
     *
     * @param overflowListId
     * @return
     */
    @Query(value = "select * from t_overflow_list_goods where overflow_list_id=?1", nativeQuery = true)
    List<OverflowListGoods> listByOverflowListId(Integer overflowListId);

    /**
     * 删除指定报溢单的所有商品
     *
     * @param overflowListId
     */
    @Query(value = "delete from t_overflow_list_goods where overflow_list_id=?1", nativeQuery = true)
    @Modifying
    void deleteByOverflowListId(Integer overflowListId);
}
