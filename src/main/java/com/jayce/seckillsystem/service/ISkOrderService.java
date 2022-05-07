package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.SkOrder;
import com.jayce.seckillsystem.entity.User;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface ISkOrderService extends IService<SkOrder> {

    /**
     * 生成秒杀路径
     *
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 返回秒杀路径
     *
     **/
    String createPath(Long userId, Long goodsId);

    /**
     * 检查秒杀路径
     *
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @param path 秒杀路径
     * @return true 秒杀路径正确
     *
     **/
    boolean checkPath(Long userId, Long goodsId, String path);

    /**
     * 获取购买结果
     *
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 订单ID
     *
     **/
    Long getResult(Long userId, Long goodsId);
}
