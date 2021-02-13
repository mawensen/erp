package com.yufeng.service.impl;

import com.yufeng.entity.Log;
import com.yufeng.repository.LogRepository;
import com.yufeng.repository.UserRepository;
import com.yufeng.service.LogService;
import com.yufeng.util.StringUtil;
import org.apache.shiro.SecurityUtils;
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
import java.util.Date;
import java.util.List;

/**
 * 系统日志Service实现类
 *
 * @author Wensen Ma
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    @Resource
    private LogRepository logRepository;

    @Resource
    private UserRepository userRepository;

    @Override
    public void save(Log log) {
        log.setTime(new Date()); // 设置操作日期
        log.setUser(userRepository.findByUserName((String) SecurityUtils.getSubject().getPrincipal())); // 设置用户名
        logRepository.save(log);
    }

    @Override
    public List<Log> list(Log log, Integer page, Integer pageSize, Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<Log> pageLog = logRepository.findAll(new Specification<Log>() {

            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (log != null) {
                    if (log.getUser() != null && StringUtil.isNotEmpty(log.getUser().getTrueName())) {
                        predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%" + log.getUser().getTrueName() + "%"));
                    }
                    if (StringUtil.isNotEmpty(log.getType())) {
                        predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
                    }
                    if (log.getBtime() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if (log.getEtime() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageLog.getContent();
    }

    @Override
    public Long getCount(Log log) {
        Long count = logRepository.count(new Specification<Log>() {

            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (log != null) {
                    if (log.getUser() != null && StringUtil.isNotEmpty(log.getUser().getTrueName())) {
                        predicate.getExpressions().add(cb.like(root.get("user").get("trueName"), "%" + log.getUser().getTrueName() + "%"));
                    }
                    if (StringUtil.isNotEmpty(log.getType())) {
                        predicate.getExpressions().add(cb.equal(root.get("type"), log.getType()));
                    }
                    if (log.getBtime() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("time"), log.getBtime()));
                    }
                    if (log.getEtime() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("time"), log.getEtime()));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

}
