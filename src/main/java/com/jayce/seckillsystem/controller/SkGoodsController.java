package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  商品信息接口
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@RestController
@Api(tags = "商品信息接口", value = "获取秒杀商品列表，商品详情信息")
@RequestMapping("/api/sk-goods")
public class SkGoodsController {

    @Resource
    IGoodsService goodsService;


    @ApiOperation("获取商品列表")
    @GetMapping("")
    public RestBean<?> skGoodsCategory() {
        List<GoodsVo> goodsList = goodsService.getGoodsList();
        return RestBean.success(goodsList);
    }


    @ApiOperation("获取商品详情信息")
    @GetMapping("/detail/goodsId={goodsId}")
    public RestBean<?> toDetail(@PathVariable Long goodsId) {
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        if(goodsVo == null) {
            return RestBean.failed(RestBeanEnum.GET_GOODS_NOT_FOUND);
        }
        return RestBean.success(goodsVo);
    }
}
