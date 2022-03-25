package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.service.IGoodsService;
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
 *  前端控制器
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@RestController
@RequestMapping("/api/sk-goods")
public class SkGoodsController {

    @Resource
    IGoodsService goodsService;

    @Resource
    RedisTemplate redisTemplate;

    /**
    * @Description 获取秒杀商品列表
    *
    * @Param []
    * @return
    *
    * @Author YoungSong
    **/
    @GetMapping("")
    public RestBean<List<GoodsVo>> skGoodsCategory() {
        List<GoodsVo> goodsList = goodsService.getGoodsList();
        return RestBean.success(goodsList);
    }

    /**
    * @Description 进入商品详情页面
    *
    * @Param [goodsId]
    * @return
    *
    * @Author YoungSong
    **/
    @GetMapping("/detail/goodsId={goodsId}")
    public RestBean<?> toDetail(@PathVariable Long goodsId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        if(goodsVo == null) {
            return RestBean.failed(RestBeanEnum.GET_GOODS_NOT_FOUND);
        }
        return RestBean.success(goodsVo);
    }
}
