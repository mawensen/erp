package com.yufeng.service.impl;

import com.yufeng.entity.DamageList;
import com.yufeng.entity.DamageListGoods;
import com.yufeng.entity.Goods;
import com.yufeng.repository.DamageListGoodsRepository;
import com.yufeng.repository.DamageListRepository;
import com.yufeng.repository.GoodsRepository;
import com.yufeng.repository.GoodsTypeRepository;
import com.yufeng.service.DamageListService;
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
 * 报损单Service实现类
 *
 * @author Wensen Ma
 */
@Service("damageListService")
@Transactional
public class DamageListServiceImpl implements DamageListService {

    @Resource
    private DamageListRepository damageListRepository;

    @Resource
    private DamageListGoodsRepository damageListGoodsRepository;

    @Resource
    private GoodsRepository goodsRepository;

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Override
    public String getTodayMaxDamageNumber() {
        return damageListRepository.getTodayMaxDamageNumber();
    }

    @Transactional
    public void save(DamageList damageList, List<DamageListGoods> damageListGoodsList) {
        // 保存每个报损单商品
        for (DamageListGoods damageListGoods : damageListGoodsList) {
            damageListGoods.setType(goodsTypeRepository.findOne(damageListGoods.getTypeId())); // 设置类别
            damageListGoods.setDamageList(damageList); // 设置采购单
            damageListGoodsRepository.save(damageListGoods);
            // 修改商品库存
            Goods goods = goodsRepository.findOne(damageListGoods.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() - damageListGoods.getNum());
            goods.setState(2);
            goodsRepository.save(goods);
        }
        damageListRepository.save(damageList); // 保存报损单
    }

    @Override
    public List<DamageList> list(DamageList damageList, Direction direction,
                                 String... properties) {
        return damageListRepository.findAll(new Specification<DamageList>() {

            @Override
            public Predicate toPredicate(Root<DamageList> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (damageList != null) {
                    if (damageList.getbDamageDate() != null) {
                        predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("damageDate"), damageList.getbDamageDate()));
                    }
                    if (damageList.geteDamageDate() != null) {
                        predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("damageDate"), damageList.geteDamageDate()));
                    }
                }
                return predicate;
            }
        }, new Sort(direction, properties));
    }

    @Override
    public void delete(Integer id) {
        damageListGoodsRepository.deleteByDamageListId(id);
        damageListRepository.delete(id);
    }

    @Override
    public DamageList findById(Integer id) {
        return damageListRepository.findOne(id);
    }


}
