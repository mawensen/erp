package com.yufeng.service.impl;

import com.yufeng.entity.User;
import com.yufeng.repository.UserRepository;
import com.yufeng.service.UserService;
import com.yufeng.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 用户Service实现类
 *
 * @author Wensen Ma
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;


    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> list(User user, Integer page, Integer pageSize, Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<User> pageUser = userRepository.findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (user != null) {
                    if (StringUtil.isNotEmpty(user.getUserName())) {
                        predicate.getExpressions().add(cb.like(root.get("userName"), "%" + user.getUserName().trim() + "%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
                }
                return predicate;
            }
        }, pageable);
        return pageUser.getContent();
    }

    @Override
    public Long getCount(User user) {
        Long count = userRepository.count(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (user != null) {
                    if (StringUtil.isNotEmpty(user.getUserName())) {
                        predicate.getExpressions().add(cb.like(root.get("userName"), "%" + user.getUserName().trim() + "%"));
                    }
                    predicate.getExpressions().add(cb.notEqual(root.get("id"), 1)); // 管理员除外
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(id);
    }


}
