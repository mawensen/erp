package com.yufeng.repository;

import com.yufeng.entity.DamageListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 报损单商品Repository接口
 *
 * @author Wensen Ma
 */
public interface DamageListGoodsRepository extends JpaRepository<DamageListGoods, Integer>, JpaSpecificationExecutor<DamageListGoods> {

    /**
     * 根据报损单id查询所有报损单商品
     *
     * @param damageListId
     * @return
     */
    @Query(value = "select * from t_damage_list_goods where damage_list_id=?1", nativeQuery = true)
    List<DamageListGoods> listByDamageListId(Integer damageListId);

    /**
     * 删除指定报损单的所有商品
     *
     * @param damageListId
     */
    @Query(value = "delete from t_damage_list_goods where damage_list_id=?1", nativeQuery = true)
    @Modifying
    void deleteByDamageListId(Integer damageListId);
}
