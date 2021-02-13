package com.yufeng.service.impl;

import com.yufeng.entity.Goods;
import com.yufeng.entity.OverflowList;
import com.yufeng.entity.OverflowListGoods;
import com.yufeng.repository.GoodsRepository;
import com.yufeng.repository.GoodsTypeRepository;
import com.yufeng.repository.OverflowListGoodsRepository;
import com.yufeng.repository.OverflowListRepository;
import com.yufeng.service.OverflowListService;
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
 * 报溢单Service实现类
 *
 * @author Wensen Ma
 */
@Service("overflowListService")
@Transactional
public class OverflowListServiceImpl implements OverflowListService {

    @Resource
    private OverflowListRepository overflowListRepository;

    @Resource
    private OverflowListGoodsRepository overflowListGoodsRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Override
    public String getTodayMaxOverflowNumber() {
        return overflowListRepository.getTodayMaxOverflowNumber();
    }

    @Transactional
    public void save(OverflowList overflowList, List<OverflowListGoods> overflowListGoodsList) {
        // 保存每个报溢单商品
        for (OverflowListGoods overflowListGoods : overflowListGoodsList) {
            overflowListGoods.setType(goodsTypeRepository.findOne(overflowListGoods.getTypeId())); // 设置类别
            overflowListGoods.setOverflowList(overflowList); // 设置采购单
            overflowListGoodsRepository.save(overflowListGoods);
            // 修改商品库存
            Goods goods = goodsRepository.findOne(overflowListGoods.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() + overflowListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        overflowListRepository.save(overflowList); // 保存报溢单
    }

    @Override
    public List<OverflowList> list(OverflowList overflowList, Direction direction,
                                   String... properties) {
        return overflowListRepository.findAll(new Specification<OverflowList>() {

            @Override
            public Predicate toPredicate(Root<OverflowList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (overflowList != null) {
                    if (overflowList.getbOverflowDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("overflowDate"), overflowList.getbOverflowDate()));
                    }
                    if (overflowList.geteOverflowDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("overflowDate"), overflowList.geteOverflowDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public void delete(Integer id) {
        overflowListGoodsRepository.deleteByOverflowListId(id);
        overflowListRepository.delete(id);
    }

    @Override
    public OverflowList findById(Integer id) {
        return overflowListRepository.findOne(id);
    }


}
