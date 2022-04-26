package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.SkGoodsMapper;
import com.jayce.seckillsystem.entity.SkGoods;
import com.jayce.seckillsystem.service.ISkGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 秒杀产品表 服务实现类
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
@Service
public class SkGoodsServiceImpl extends ServiceImpl<SkGoodsMapper, SkGoods> implements ISkGoodsService {

    @Resource
    private SkGoodsMapper skGoodsMapper;

    @Override
    public int getStock(Long goodsId) {
        return skGoodsMapper.getStock(goodsId);
    }

    @Override
    public int reduceStock(SkGoods skGoods) {

        return skGoodsMapper.reduceStock(skGoods);
    }

    @Override
    public SkGoods getByGoodsId(Long goodsId) {
        return skGoodsMapper.getByGoodsId(goodsId);
    }

    @Override
    public int rollbackStock(SkGoods skGoods) {
        return skGoodsMapper.rollbackStock(skGoods);
    }
}
