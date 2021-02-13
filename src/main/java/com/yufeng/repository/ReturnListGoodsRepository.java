package com.yufeng.repository;

import com.yufeng.entity.ReturnListGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 退货单商品Repository接口
 *
 * @author Wensen Ma
 */
public interface ReturnListGoodsRepository extends JpaRepository<ReturnListGoods, Integer>, JpaSpecificationExecutor<ReturnListGoods> {

    /**
     * 根据退货单id查询所有退货单商品
     *
     * @param returnListId
     * @return
     */
    @Query(value = "select * from t_return_list_goods where return_list_id=?1", nativeQuery = true)
    List<ReturnListGoods> listByReturnListId(Integer returnListId);

    /**
     * 删除指定退货单的所有商品
     *
     * @param returnListId
     */
    @Query(value = "delete from t_return_list_goods where return_list_id=?1", nativeQuery = true)
    @Modifying
    void deleteByReturnListId(Integer returnListId);
}