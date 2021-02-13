package com.yufeng.repository;

import com.yufeng.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 用户Repository接口
 *
 * @author Wensen Ma
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找用户实体
     *
     * @param userName
     * @return
     */
    @Query(value = "select * from t_user where user_name=?1", nativeQuery = true)
	User findByUserName(String userName);
}
