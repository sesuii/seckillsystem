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

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId, String path);

    Long getResult(User user, Long goodsId);
}
