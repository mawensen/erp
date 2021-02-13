package com.yufeng.service.impl;

import com.yufeng.entity.CustomerReturnListGoods;
import com.yufeng.repository.CustomerReturnListGoodsRepository;
import com.yufeng.service.CustomerReturnListGoodsService;
import com.yufeng.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 客户退货单商品Service实现类
 *
 * @author Wensen Ma
 */
@Service("customerReturnListGoodsService")
public class CustomerReturnListGoodsServiceImpl implements CustomerReturnListGoodsService {

    @Resource
    private CustomerReturnListGoodsRepository customerReturnListGoodsRepository;

    @Override
    public List<CustomerReturnListGoods> listByCustomerReturnListId(Integer customerReturnListId) {
        return customerReturnListGoodsRepository.listByCustomerReturnListId(customerReturnListId);
    }

    @Override
    public Integer getTotalByGoodsId(Integer goodsId) {
        return customerReturnListGoodsRepository.getTotalByGoodsId(goodsId) == null ? 0 : customerReturnListGoodsRepository.getTotalByGoodsId(goodsId);
    }

    @Override
    public List<CustomerReturnListGoods> list(CustomerReturnListGoods customerReturnListGoods) {
        return customerReturnListGoodsRepository.findAll(new Specification<CustomerReturnListGoods>() {

            @Override
            public Predicate toPredicate(Root<CustomerReturnListGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (customerReturnListGoods != null) {
                    if (customerReturnListGoods.getType() != null && customerReturnListGoods.getType().getId() != null && customerReturnListGoods.getType().getId() != 1) {
                        predicate.getExpressions().add(cb.equal(root.get("type").get("id"), customerReturnListGoods.getType().getId()));
                    }
                    if (StringUtil.isNotEmpty(customerReturnListGoods.getCodeOrName())) {
                        predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + customerReturnListGoods.getCodeOrName() + "%"), cb.like(root.get("name"), "%" + customerReturnListGoods.getCodeOrName() + "%")));
                    }
                    if (customerReturnListGoods.getCustomerReturnList() != null && StringUtil.isNotEmpty(customerReturnListGoods.getCustomerReturnList().getCustomerReturnNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("customerReturnList").get("customerReturnNumber"), "%" + customerReturnListGoods.getCustomerReturnList().getCustomerReturnNumber() + "%"));
                    }
                }
                return predicate;
            }
        });
    }


}
