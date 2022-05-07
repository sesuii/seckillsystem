package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.OrderStatusConstant;
import com.jayce.seckillsystem.constant.ResultEnum;
import com.jayce.seckillsystem.entity.OrderInfo;
import com.jayce.seckillsystem.entity.TradeRecord;
import com.jayce.seckillsystem.entity.User;
import com.jayce.seckillsystem.entity.resp.Result;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;
import com.jayce.seckillsystem.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;

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
        if(orderInfoVo == null) {
            return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        }
        return Result.success(orderInfoVo);
    }

    @ApiOperation("获取订单状态")
    @GetMapping("/order-status")
    public Result<?> getOrderStatus(Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(orderInfo == null) {
            return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        }
        if(orderInfo.getStatus() == OrderStatusConstant.ORDER_JUST_CREATE) {
            return Result.failed(ResultEnum.ORDER_NOT_PAY);
        }
        if(orderInfo.getStatus() == OrderStatusConstant.ORDER_HAS_CANCELED) {
            return Result.failed(ResultEnum.ORDER_HAS_CANCEL);

        }
        if(orderInfo.getStatus() == OrderStatusConstant.ORDER_HAS_PAID) {
            return Result.success("订单已支付");
        }
        return Result.success("UNKNOWN");
    }

    @ApiOperation("取消订单")
    @GetMapping("/order-cancel{orderId}")
    public Result<?> orderCancel(@PathVariable Long orderId) {
        OrderInfo orderInfo = orderInfoService.getById(orderId);
        if(null == orderInfo) {
            return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        }
        boolean isCanceledOrder = orderInfoService.cancelOrder(orderInfo);
        if(isCanceledOrder) {
            return Result.success();
        }
        return Result.failed(ResultEnum.FAILED);
    }

    @ApiOperation("用户支付订单")
    @GetMapping("/payment")
    public Result<?> payOrder(Long orderId) {
        OrderInfo order = orderInfoService.getById(orderId);
        if(!checkOrder(order)) {
            return Result.failed(ResultEnum.ORDER_ERROR);
        }
        boolean isPaidSuccess;
        try {
            isPaidSuccess = orderInfoService.payOrder(order);
        } catch (Throwable e) {
            return Result.failed(ResultEnum.FAILED);
        }
        if(isPaidSuccess) {
            return Result.success();
        }
        return Result.failed(ResultEnum.FAILED);
    }

    @ApiOperation("获取交易记录")
    @GetMapping("/trade-record{orderId}")
    public Result<?> getTradeRecord(@PathVariable Long orderId) {
        TradeRecord tradeRecord = orderInfoService.getTradeRecord(orderId);
        if(tradeRecord == null) {
            return Result.failed(ResultEnum.ORDER_NOT_EXIST);
        }
        return Result.success(tradeRecord);
    }

    /**
     * 检查订单
     *
     * @param order 订单信息
     * @return
     *
     **/
    private boolean checkOrder(OrderInfo order) {
        if(order == null) {
            return false;
        }
        return order.getStatus() == OrderStatusConstant.ORDER_JUST_CREATE;
    }
}
