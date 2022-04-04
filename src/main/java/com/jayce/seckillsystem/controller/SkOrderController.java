package com.jayce.seckillsystem.controller;


import com.jayce.seckillsystem.constant.RestBeanEnum;
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
        OrderInfoVo orderInfoVo = orderInfoService.detail(orderId);
        return RestBean.success(orderInfoVo);
    }
}
