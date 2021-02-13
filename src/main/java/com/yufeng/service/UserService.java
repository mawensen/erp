package com.yufeng.service;

import com.yufeng.entity.User;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

/**
 * 用户Service接口
 *
 * @author Wensen Ma
 */
public interface UserService {

    /**
     * 根据用户名查找用户实体
     *
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * 根据id查询用户实体
     *
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 修改或者修改用户信息
     *
     * @param user
     */
    void save(User user);

    /**
     * 根据条件分页查询用户信息
     *
     * @param user
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    List<User> list(User user, Integer page, Integer pageSize, Direction direction, String... properties);

    /**
     * 获取总记录数
     *
     * @param user
     * @return
     */
    Long getCount(User user);

    /**
     * 根据id删除用户
     *
     * @param id
     */
    void delete(Integer id);
}
