package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    OrderInfoVo detail(Long orderId);
}
