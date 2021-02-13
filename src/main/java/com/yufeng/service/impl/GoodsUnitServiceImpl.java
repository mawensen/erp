package com.yufeng.service.impl;

import com.yufeng.entity.GoodsUnit;
import com.yufeng.repository.GoodsUnitRepository;
import com.yufeng.service.GoodsUnitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品单位Service实现类
 *
 * @author Wensen Ma
 */
@Service("goodsUnitService")
public class GoodsUnitServiceImpl implements GoodsUnitService {

    @Resource
    private GoodsUnitRepository goodsUnitRepository;

    @Override
    public List<GoodsUnit> listAll() {
        return goodsUnitRepository.findAll();
    }

    @Override
    public void save(GoodsUnit goodsUnit) {
        goodsUnitRepository.save(goodsUnit);
    }

    @Override
    public void delete(Integer id) {
        goodsUnitRepository.delete(id);
    }

    @Override
    public GoodsUnit findById(Integer id) {
        return goodsUnitRepository.findOne(id);
    }

}
