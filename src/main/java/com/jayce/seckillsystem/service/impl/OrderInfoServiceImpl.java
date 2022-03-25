package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.service.IOrderInfoService;
import org.springframework.stereotype.Service;

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

}
