package com.jayce.seckillsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayce.seckillsystem.entity.SkGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 秒杀产品表 Mapper 接口
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
public interface SkGoodsMapper extends BaseMapper<SkGoods> {

    /**
     * 获取某商品的库存
     *
     * @param goodsId 商品ID
     * @return 商品库存
     */
    @Select("select stock from sk_goods where id = #{goodsId} ")
    int getStock(@Param("goodsId") Long goodsId);

    /**
     * 扣减商品库存
     *
     * @param skGoods 商品
     */
//    @Update("update sk_goods set stock = stock - 1, version = version + 1 where id = #{id} and version = #{version}")
    @Update("update sk_goods set stock = stock - 1 where id = #{id} and stock > 0")
    int reduceStock(SkGoods skGoods);
}
