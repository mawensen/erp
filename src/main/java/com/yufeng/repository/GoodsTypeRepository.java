package com.yufeng.repository;

import com.yufeng.entity.GoodsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品类别Repository接口
 *
 * @author Wensen Ma
 */
public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer>, JpaSpecificationExecutor<GoodsType> {

    /**
     * 根据父节点查找商品类别
     *
     * @param parentId
     * @return
     */
    @Query(value = "select * from t_goodstype where p_id=?1", nativeQuery = true)
	List<GoodsType> findByParentId(int parentId);
}
