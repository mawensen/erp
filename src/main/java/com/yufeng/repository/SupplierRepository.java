package com.yufeng.repository;

import com.yufeng.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 供应商Repository接口
 *
 * @author Wensen Ma
 */
public interface SupplierRepository extends JpaRepository<Supplier, Integer>, JpaSpecificationExecutor<Supplier> {

    /**
     * 根据名称模糊查询供应商信息
     *
     * @param name
     * @return
     */
    @Query(value = "select * from t_supplier where name like ?1", nativeQuery = true)
    List<Supplier> findByName(String name);

}
