package com.yufeng.service.impl;

import com.yufeng.entity.DamageListGoods;
import com.yufeng.repository.DamageListGoodsRepository;
import com.yufeng.service.DamageListGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报损单商品Service实现类
 *
 * @author Wensen Ma
 */
@Service("damageListGoodsService")
public class DamageListGoodsServiceImpl implements DamageListGoodsService {

    @Resource
    private DamageListGoodsRepository damageListGoodsRepository;

    @Override
    public List<DamageListGoods> listByDamageListId(Integer damageListId) {
        return damageListGoodsRepository.listByDamageListId(damageListId);
    }


}
