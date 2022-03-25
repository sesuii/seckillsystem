package com.jayce.seckillsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayce.seckillsystem.entity.Goods;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    @Select("select * from goods join sk_goods on goods.id = sk_goods.goods_id")
    List<GoodsVo> getGoodsList();

    @Select("select * from goods join sk_goods on goods.id = sk_goods.goods_id where goods.id = #{goodsId}")
    GoodsVo findGoodsVoById(@Param("goodsId") Long goodsId);
}
