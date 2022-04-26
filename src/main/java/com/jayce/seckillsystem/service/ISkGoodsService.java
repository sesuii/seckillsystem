package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.SkGoods;

/**
 * 商品管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public interface ISkGoodsService extends IService<SkGoods> {

    int getStock(Long goodsId);

    int reduceStock(SkGoods skGoods);

    SkGoods getByGoodsId(Long goodsId);

    int rollbackStock(SkGoods skGoods);
}
