package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.SkGoodsMapper;
import com.jayce.seckillsystem.domain.SkGoods;
import com.jayce.seckillsystem.service.SkGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商品管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Service
public class SkGoodsServiceImpl extends ServiceImpl<SkGoodsMapper, SkGoods> implements SkGoodsService {

    @Resource
    private SkGoodsMapper skGoodsMapper;

    @Override
    public int getStock(Integer goodsId) {
        return skGoodsMapper.getStock(goodsId);
    }

    @Override
    public int reduceStock(SkGoods skGoods) {

        return skGoodsMapper.reduceStock(skGoods);
    }
}
