package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.SkGoods;

/**
 * 商品管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public interface ISkGoodsService extends IService<SkGoods> {

    /**
     * 获取商品库存
     *
     * @param goodsId 商品ID
     * @return
     *
     **/
    int getStock(Long goodsId);

    /**
     * 减少商品库存
     *
     * @param skGoods 秒杀商品
     * @return
     *
     **/
    int reduceStock(SkGoods skGoods);

    /**
     * 通过商品ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return
     *
     **/
    SkGoods getByGoodsId(Long goodsId);

    /**
     * 回滚商品库存
     *
     * @param skGoods 秒杀商品
     * @return
     *
     **/
    int rollbackStock(SkGoods skGoods);
}
