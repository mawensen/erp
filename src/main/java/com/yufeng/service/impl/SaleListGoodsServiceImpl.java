package com.yufeng.service.impl;

import com.yufeng.entity.SaleListGoods;
import com.yufeng.repository.SaleListGoodsRepository;
import com.yufeng.service.SaleListGoodsService;
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
 * 销售单商品Service实现类
 *
 * @author Wensen Ma
 */
@Service("saleListGoodsService")
public class SaleListGoodsServiceImpl implements SaleListGoodsService {

    @Resource
    private SaleListGoodsRepository saleListGoodsRepository;

    @Override
    public List<SaleListGoods> listBySaleListId(Integer saleListId) {
        return saleListGoodsRepository.listBySaleListId(saleListId);
    }

    @Override
    public Integer getTotalByGoodsId(Integer goodsId) {
        return saleListGoodsRepository.getTotalByGoodsId(goodsId) == null ? 0 : saleListGoodsRepository.getTotalByGoodsId(goodsId);
    }

    @Override
    public List<SaleListGoods> list(SaleListGoods saleListGoods) {
        return saleListGoodsRepository.findAll(new Specification<SaleListGoods>() {

            @Override
            public Predicate toPredicate(Root<SaleListGoods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (saleListGoods != null) {
                    if (saleListGoods.getType() != null && saleListGoods.getType().getId() != null && saleListGoods.getType().getId() != 1) {
                        predicate.getExpressions().add(cb.equal(root.get("type").get("id"), saleListGoods.getType().getId()));
                    }
                    if (StringUtil.isNotEmpty(saleListGoods.getCodeOrName())) {
                        predicate.getExpressions().add(cb.or(cb.like(root.get("code"), "%" + saleListGoods.getCodeOrName() + "%"), cb.like(root.get("name"), "%" + saleListGoods.getCodeOrName() + "%")));
                    }
                    if (saleListGoods.getSaleList() != null && StringUtil.isNotEmpty(saleListGoods.getSaleList().getSaleNumber())) {
                        predicate.getExpressions().add(cb.like(root.get("saleList").get("saleNumber"), "%" + saleListGoods.getSaleList().getSaleNumber() + "%"));
                    }
                }
                return predicate;
            }
        });
    }


}
