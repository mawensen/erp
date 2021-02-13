package com.yufeng.repository;

import com.yufeng.entity.DamageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 报损单Repository接口
 *
 * @author Wensen Ma
 */
public interface DamageListRepository extends JpaRepository<DamageList, Integer>, JpaSpecificationExecutor<DamageList> {

    /**
     * 获取当天最大报损单号
     *
     * @return
     */
    @Query(value = "SELECT MAX(damage_number) FROM t_damage_list WHERE TO_DAYS(damage_date) = TO_DAYS(NOW())", nativeQuery = true)
    String getTodayMaxDamageNumber();
}
