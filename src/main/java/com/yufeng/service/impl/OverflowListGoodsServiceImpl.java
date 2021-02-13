package com.yufeng.service.impl;

import com.yufeng.entity.OverflowListGoods;
import com.yufeng.repository.OverflowListGoodsRepository;
import com.yufeng.service.OverflowListGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 报溢单商品Service实现类
 *
 * @author Wensen Ma
 */
@Service("overflowListGoodsService")
public class OverflowListGoodsServiceImpl implements OverflowListGoodsService {

    @Resource
    private OverflowListGoodsRepository overflowListGoodsRepository;

    @Override
    public List<OverflowListGoods> listByOverflowListId(Integer overflowListId) {
        return overflowListGoodsRepository.listByOverflowListId(overflowListId);
    }


}
