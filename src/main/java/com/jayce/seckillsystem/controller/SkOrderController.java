package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;
import com.jayce.seckillsystem.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@RestController
@RequestMapping("/api/sk-order")
@Api(tags = "订单接口")
public class SkOrderController {
    @Resource
    IOrderInfoService orderInfoService;

    @ApiOperation("获取订单详细信息")
    @GetMapping("/detail")
    public RestBean<?> detail(User user, Long orderId) {
        if (user == null) {
            return RestBean.failed(RestBeanEnum.AUTH_DENY);
        }
        return orderInfoService.detail(orderId);
    }

    @ApiOperation("获取订单状态")
    @GetMapping("/getOrderStatus")
    public RestBean<?> getOrderStatus(Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(orderInfo == null) return RestBean.failed(RestBeanEnum.FAILED, "订单不存在");
        if(orderInfo.getStatus() == 0) {
            return RestBean.failed(RestBeanEnum.ORDER_NOT_PAY);
        }
        else if(orderInfo.getStatus() == 1)
            return RestBean.success("订单已完成");
        else if(orderInfo.getStatus() == 2)
            return RestBean.failed(RestBeanEnum.ORDER_HAS_CANCEL);
        return null;
    }

    @ApiOperation("取消订单")
    @GetMapping("orderCancel{orderId}")
    public RestBean<?> orderCancel(@PathParam("orderId") Long orderId) throws Exception {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(null == orderInfo) return RestBean.failed(RestBeanEnum.ORDER_NOT_EXIST);
        return orderInfoService.cancelOrder(orderInfo);
    }

    @ApiOperation("用户支付订单")
    @GetMapping("/payFor")
    public RestBean<?> payOrder(Long orderId) throws Exception {
        return orderInfoService.payOrder(orderId);
    }
}
