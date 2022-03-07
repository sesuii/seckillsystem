package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.domain.SkGoods;

/**
 * 商品管理
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
public interface SkGoodsService extends IService<SkGoods> {

    /**
     * 获取某商品的库存
     *
     * @param goodsId 商品ID
     * @return 商品库存
     */
    int getStock(Integer goodsId);

    /**
     * 扣减商品库存
     *
     * @param skGoods 商品
     */
    int reduceStock(SkGoods skGoods);
}
