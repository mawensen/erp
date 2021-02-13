package com.yufeng.repository;

import com.yufeng.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 客户Repository接口
 *
 * @author Wensen Ma
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据名称模糊查询客户信息
     *
     * @param name
     * @return
     */
    @Query(value = "select * from t_customer where name like ?1", nativeQuery = true)
	List<Customer> findByName(String name);
}
