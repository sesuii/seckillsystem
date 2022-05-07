package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.Goods;
import com.jayce.seckillsystem.entity.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 获取商品列表
     *
     * @return
     *
     **/
    List<GoodsVo> getGoodsList();

    /**
     * 通过商品ID获取商品信息
     *
     * @param goodsId 商品ID
     * @return
     *
     **/
    GoodsVo findGoodsVoById(Long goodsId);
}
