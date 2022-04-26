package com.jayce.seckillsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayce.seckillsystem.entity.OrderInfo;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
    * @Description 取消订单
    *
    * @param orderInfo 订单详情
    * @return 操作是否成功，1 为成功
    *
    **/
    @Update("update order_info set status = 2 where id = #{id} and status != 1")
    int cancelOrder(OrderInfo orderInfo);
}
