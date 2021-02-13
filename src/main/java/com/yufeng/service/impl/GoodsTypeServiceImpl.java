package com.yufeng.service.impl;

import com.yufeng.entity.GoodsType;
import com.yufeng.repository.GoodsTypeRepository;
import com.yufeng.service.GoodsTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品类别Service实现类
 *
 * @author Wensen Ma
 */
@Service("goodsTypeService")
public class GoodsTypeServiceImpl implements GoodsTypeService {

    @Resource
    private GoodsTypeRepository goodsTypeRepository;

    @Override
    public void save(GoodsType goodsType) {
        goodsTypeRepository.save(goodsType);
    }

    @Override
    public void delete(Integer id) {
        goodsTypeRepository.delete(id);
    }

    @Override
    public List<GoodsType> findByParentId(int parentId) {
        return goodsTypeRepository.findByParentId(parentId);
    }

    @Override
    public GoodsType findById(Integer id) {
        return goodsTypeRepository.findOne(id);
    }

}
