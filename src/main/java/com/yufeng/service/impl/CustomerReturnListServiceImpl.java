package com.yufeng.service.impl;

import com.yufeng.entity.CustomerReturnList;
import com.yufeng.entity.CustomerReturnListGoods;
import com.yufeng.entity.Goods;
import com.yufeng.repository.CustomerReturnListGoodsRepository;
import com.yufeng.repository.CustomerReturnListRepository;
import com.yufeng.repository.GoodsRepository;
import com.yufeng.repository.GoodsTypeRepository;
import com.yufeng.service.CustomerReturnListService;
import com.yufeng.util.StringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 销售单Service实现类
 *
 * @author Wensen Ma
 */
@Service("customerReturnListService")
@Transactional
public class CustomerReturnListServiceImpl implements CustomerReturnListService {

    @Resource
    private CustomerReturnListRepository customerReturnListRepository;

    @Resource
    private CustomerReturnListGoodsRepository customerReturnListGoodsRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Override
    public String getTodayMaxCustomerReturnNumber() {
        return customerReturnListRepository.getTodayMaxCustomerReturnNumber();
    }

    @Transactional
    public void save(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> customerReturnListGoodsList) {
        // 保存每个销售单商品
        for (CustomerReturnListGoods customerReturnListGoods : customerReturnListGoodsList) {
            customerReturnListGoods.setType(goodsTypeRepository.findOne(customerReturnListGoods.getTypeId())); // 设置类别
            customerReturnListGoods.setCustomerReturnList(customerReturnList); // 设置采购单
            customerReturnListGoodsRepository.save(customerReturnListGoods);
            // 修改商品库存
            Goods goods = goodsRepository.findOne(customerReturnListGoods.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() + customerReturnListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        customerReturnListRepository.save(customerReturnList); // 保存销售单
    }

    @Override
    public List<CustomerReturnList> list(CustomerReturnList customerReturnList, Direction direction,
                                         String... properties) {
        return customerReturnListRepository.findAll(new Specification<CustomerReturnList>() {

            @Override
            public Predicate toPredicate(Root<CustomerReturnList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (customerReturnList != null) {
                    if (customerReturnList.getCustomer() != null && customerReturnList.getCustomer().getId() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("customer").get("id"), customerReturnList.getCustomer().getId()));
                    }
                    if (StringUtil.isNotEmpty(customerReturnList.getCustomerReturnNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("customerReturnNumber"), "%" + customerReturnList.getCustomerReturnNumber().trim() + "%"));
                    }
                    if (customerReturnList.getState() != null) {
                        predicate.getExpressions().add(cb.equal(root.get("state"), customerReturnList.getState()));
                    }
                    if (customerReturnList.getbCustomerReturnDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.getbCustomerReturnDate()));
                    }
                    if (customerReturnList.geteCustomerReturnDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("customerReturnDate"), customerReturnList.geteCustomerReturnDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public void delete(Integer id) {
        customerReturnListGoodsRepository.deleteByCustomerReturnListId(id);
        customerReturnListRepository.delete(id);
    }

    @Override
    public CustomerReturnList findById(Integer id) {
        return customerReturnListRepository.findOne(id);
    }

    @Override
    public void update(CustomerReturnList customerReturnList) {
        customerReturnListRepository.save(customerReturnList);
    }


}
