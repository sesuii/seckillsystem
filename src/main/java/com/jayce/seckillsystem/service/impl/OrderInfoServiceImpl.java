package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.constant.RestBeanEnum;
import com.jayce.seckillsystem.entity.*;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.entity.resp.RestBean;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;
import com.jayce.seckillsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@Slf4j
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    @Resource
    IOrderInfoService orderInfoService;

    @Resource
    IGoodsService goodsService;

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Resource
    ISkOrderService skOrderService;

    @Resource
    IBankAccountService bankAccountService;

    @Resource
    IUserFinancialService userFinancialService;

    @Resource
    ITradeRecordService tradeRecordService;

    /**
    * @Description 获取订单详情信息
    *
    * @param orderId 订单号
    * @return
    *
    **/
    @Override
    public RestBean<?> detail(Long orderId) {
        OrderInfo order = orderInfoService.getById(orderId);
        if(order == null) return RestBean.failed(RestBeanEnum.ORDER_NOT_EXIST);
        GoodsVo goodsVo = goodsService.findGoodsVoById(order.getGoodsId());
        OrderInfoVo orderInfoVo = OrderInfoVo.builder()
                .orderInfo(order)
                .goodsVo(goodsVo)
                .build();
        return RestBean.success(orderInfoVo);
    }

    /**
    * @Description 支付订单
    *
    * @param orderId 订单id
    * @return
    *
    **/

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<?> payOrder(Long orderId) throws Exception {
        OrderInfo order = orderInfoService.getById(orderId);
        if(order == null) return RestBean.failed(RestBeanEnum.ORDER_NOT_EXIST);
        BigDecimal pay = order.getGoodsPrice();
        if(isOrderCanceled(order)) return RestBean.failed(RestBeanEnum.ORDER_HAS_CANCEL);
        if(isOrderPayed(order)) return RestBean.success("订单已支付");
        // 更新个人账户
        UserFinancial userFinancial = userFinancialService.getById(order.getUserId());
        if(userFinancial == null) return RestBean.failed(RestBeanEnum.ORDER_NOT_EXIST);
        boolean isUpdateUser = userFinancialService.reduce(userFinancial, pay);
        if(!isUpdateUser) {
            log.info("用户账户余额不足");
            throw new Exception("用户账户余额不足");
        }
        // 更新银行账户
        BankAccount bankAccount = bankAccountService.getById(1L);
        bankAccount.setAccountBalance(bankAccount.getAccountBalance().add(pay));
        boolean isUpdateBank = bankAccountService.updateById(bankAccount);
        if(!isUpdateBank) {
            log.info("转账失败");
            throw new Exception("转账失败");
        }
        // 更新订单状态
        order.setStatus((byte) 1);
        order.setPayTime(new Date());
        boolean isUpdateOrder = orderInfoService.updateById(order);
        if(!isUpdateOrder) {
            log.info("订单异常");
            throw new Exception("订单异常");
        }
        // 创建交易记录
        TradeRecord tradeRecord = TradeRecord.builder()
                .orderId(orderId)
                .payeeId(bankAccount.getId())
                .remitterId(order.getUserId())
                .tradingTime(order.getPayTime())
                .build();

        boolean isCreateRecord = tradeRecordService.save(tradeRecord);
        if(!isCreateRecord) {
            log.info("订单异常");
            throw new Exception("订单异常");
        }
        return RestBean.success(tradeRecord);
    }

    /**
    * @Description 取消订单 删除秒杀订单内容 将订单状态设为 2
    *
    * @param orderInfo 订单
    * @return
    *
    **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestBean<?> cancelOrder(OrderInfo orderInfo) throws Exception {
        if(isOrderPayed(orderInfo)) return RestBean.failed(RestBeanEnum.FAILED, "订单已支付");
        boolean isCanceled = skOrderService.remove(new LambdaQueryWrapper<SkOrder>()
                .eq(SkOrder::getOrderInfoId, orderInfo.getId())
        );
        if(orderInfoMapper.cancel(orderInfo) == 1) isCanceled = false;
        if(!isCanceled) throw new Exception("用户订单取消失败");
        return RestBean.success("订单取消成功！");
    }

    /**
    * @Description 订单是否支付
    *
    * @param order 订单
    * @return true 已支付
    *
    **/
    private boolean isOrderPayed(OrderInfo order) {
        return order.getStatus() == 1;
    }

    /**
    * @Description 订单是否已经取消
    *
    * @param order 订单
    * @return true 已取消
    *
    **/
    private boolean isOrderCanceled(OrderInfo order) {
        return order.getStatus() == 2;
    }
}
