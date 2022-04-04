package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;
import com.jayce.seckillsystem.service.IGoodsService;
import com.jayce.seckillsystem.service.IOrderInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Resource
    IOrderInfoService orderInfoService;

    @Resource
    IGoodsService goodsService;

    @Override
    public OrderInfoVo detail(Long orderId) {
        OrderInfo order = orderInfoService.getById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoById(order.getGoodsId());
        return OrderInfoVo.builder()
                .orderInfo(order)
                .goodsVo(goodsVo)
                .build();
    }
}
