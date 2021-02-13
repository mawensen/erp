package com.yufeng.service.impl;

import com.yufeng.entity.Customer;
import com.yufeng.repository.CustomerRepository;
import com.yufeng.service.CustomerService;
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
 * 客户Service实现类
 *
 * @author Wensen Ma
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerRepository customerRepository;


    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> list(Customer customer, Integer page, Integer pageSize, Direction direction, String... properties) {
        Pageable pageable = new PageRequest(page - 1, pageSize, direction, properties);
        Page<Customer> pageCustomer = customerRepository.findAll(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (customer != null) {
                    if (StringUtil.isNotEmpty(customer.getName())) {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + customer.getName().trim() + "%"));
                    }
                }
                return predicate;
            }
        }, pageable);
        return pageCustomer.getContent();
    }

    @Override
    public Long getCount(Customer customer) {
        Long count = customerRepository.count(new Specification<Customer>() {

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (customer != null) {
                    if (StringUtil.isNotEmpty(customer.getName())) {
                        predicate.getExpressions().add(cb.like(root.get("name"), "%" + customer.getName().trim() + "%"));
                    }
                }
                return predicate;
            }
        });
        return count;
    }

    @Override
    public void delete(Integer id) {
        customerRepository.delete(id);
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findOne(id);
    }

    @Override
    public List<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }


}
