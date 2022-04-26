package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.OrderStatusConstant;
import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
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
    @GetMapping("/order-detail")
    public Result<?> toDetail(User user, Long orderId) {
        if (user == null) {
            return Result.failed(ResultEnum.AUTH_DENY);
        }
        OrderInfoVo orderInfoVo = orderInfoService.toDetail(orderId);
        if(orderInfoVo == null) return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        return Result.success(orderInfoVo);
    }

    @ApiOperation("获取订单状态")
    @GetMapping("/order-status")
    public Result<?> getOrderStatus(Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(orderInfo == null) return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        if(orderInfo.getStatus() == OrderStatusConstant.ORDER_JUST_CREATE) {
            return Result.failed(ResultEnum.ORDER_NOT_PAY);
        }
        else if(orderInfo.getStatus() == OrderStatusConstant.ORDER_HAS_PAID) {
            return Result.success("订单已支付");
        }
        else if(orderInfo.getStatus() == OrderStatusConstant.ORDER_HAS_CANCELED) {
            return Result.failed(ResultEnum.ORDER_HAS_CANCEL);

        }
        return Result.success("UNKNOWN");
    }

    @ApiOperation("取消订单")
    @GetMapping("order-cancel{orderId}")
    public Result<?> orderCancel(@PathParam("orderId") Long orderId) throws Exception {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(null == orderInfo) return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        boolean isCanceledOrder = false;
        try {
            isCanceledOrder = orderInfoService.cancelOrder(orderInfo);
        }
        catch (Exception e) {
            throw e;
        }
        if(isCanceledOrder) return Result.success();
            else return Result.failed(ResultEnum.FAILED);
    }

    @ApiOperation("用户支付订单")
    @GetMapping("/payment")
    public Result<?> payOrder(Long orderId) {
        try {
            return orderInfoService.payOrder(orderId);
        }
        catch (Exception e){
        }
        return Result.failed(ResultEnum.FAILED);
    }
}
