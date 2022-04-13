package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.service.IGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public Result<?> skGoodsCategory() {
        List<GoodsVo> goodsList = goodsService.getGoodsList();
        return Result.success(goodsList);
    }


    @ApiOperation("获取商品详情信息")
    @GetMapping("/detail/goodsId={goodsId}")
    public Result<?> toDetail(@PathVariable Long goodsId) {
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        if(goodsVo == null) {
            return Result.failed(ResultEnum.GET_GOODS_NOT_FOUND);
        }
        return Result.success(goodsVo);
    }
}
