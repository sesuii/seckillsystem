package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.constant.OrderStatusConstant;
import com.jayce.seckillsystem.constant.RedisConstant;
import com.jayce.seckillsystem.dao.OrderInfoMapper;
import com.jayce.seckillsystem.dao.UserFinancialMapper;
import com.jayce.seckillsystem.entity.*;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.entity.vo.OrderInfoVo;
import com.jayce.seckillsystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gerry
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

    @Resource
    ISkGoodsService skGoodsService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    DataSourceTransactionManager dateSourceTransactionManager;

    @Resource
    TransactionDefinition transactionDefinition;

    /**
    * 获取订单详情信息
    *
    * @param orderId 订单号
     *
    **/
    @Override
    public OrderInfoVo toDetail(Long orderId) {
        OrderInfo order = orderInfoService.getById(orderId);
        if(order == null) {
            return null;
        }
        GoodsVo goodsVo = goodsService.findGoodsVoById(order.getGoodsId());
        return OrderInfoVo.builder()
                .orderInfo(order)
                .goodsVo(goodsVo)
                .build();
    }

    /**
     * 支付订单
     *
     * @param order 订单
     * @return true 支付成功
     *
     **/
    @Override
    public boolean payOrder(OrderInfo order) {
        UserFinancial userFinancial = userFinancialService.getById(order.getUserId());
        if(userFinancial == null) {
            return false;
        }
        TransactionStatus status = dateSourceTransactionManager.getTransaction(transactionDefinition);
        boolean isSuccess = true;
        try{
            // 更新银行账户
            BankAccount bankAccount = bankAccountService.getById(1L);
            bankAccount.setAccountBalance(bankAccount.getAccountBalance().add(order.getGoodsPrice()));
            bankAccountService.updateById(bankAccount);
            // 更新订单状态
            order.setStatus((byte) OrderStatusConstant.ORDER_HAS_PAID);
            order.setPayTime(new Date());
            orderInfoService.updateById(order);
            // 创建交易记录
            TradeRecord tradeRecord = TradeRecord.builder()
                    .orderId(order.getId())
                    .payeeId(bankAccount.getId())
                    .remitterId(order.getUserId())
                    .tradingTime(order.getPayTime())
                    .build();
            tradeRecordService.save(tradeRecord);
            // 更新个人账户
            userFinancialService.reduce(userFinancial, order.getGoodsPrice());
            dateSourceTransactionManager.commit(status);
        }catch(Exception e){
            isSuccess = false;
            log.info("事务执行失败");
            dateSourceTransactionManager.rollback(status);
        }
        return isSuccess;
    }

    @Override
    public TradeRecord getTradeRecord(Long orderId) {
        return tradeRecordService.getOne(new LambdaQueryWrapper<TradeRecord>()
                .eq(TradeRecord::getOrderId, orderId)
        );
    }

    /**
    * 取消订单 删除秒杀订单内容 将订单状态设为 取消状态
    *
    * @param orderInfo 订单
    * @return
    *
    **/
    @Override
    public boolean cancelOrder(OrderInfo orderInfo) {
        if(isOrderPayed(orderInfo)) {
            return true;
        }
        boolean isCanceled = skOrderService.remove(new LambdaQueryWrapper<SkOrder>()
                .eq(SkOrder::getOrderInfoId, orderInfo.getId())
        );
        if(isCanceled && orderInfoMapper.cancelOrder(orderInfo) == 0) {
            return false;
        }
        return rollbackStock(orderInfo.getGoodsId()) && rollbackRedisStock(orderInfo.getGoodsId());
    }

    /**
    * 回滚 Redis 预减库存
    *
    * @param goodsId 商品 ID
    * @return
    *
    **/
    @Override
    public boolean rollbackRedisStock(Long goodsId) {
        Long increment = redisTemplate.opsForValue().increment(RedisConstant.GOODS_PREFIX + goodsId);
        if (increment != null && increment > 0) {
            GoodsStore.goodsSoldOut.put(goodsId, false);
        }
        return true;
    }

    /**
    * 回滚数据库库存
    *
    * @param goodsId 商品 ID
    * @return
    *
    **/
    @Override
    public boolean rollbackStock(Long goodsId) {
        SkGoods skGoods = skGoodsService.getByGoodsId(goodsId);
        int newStock = skGoods.getStock() + 1;
        skGoods.setStock(newStock);
        int update = skGoodsService.rollbackStock(skGoods);
        return update == 1;
    }

    /**
    * 订单是否支付
    *
    * @param order 订单
    * @return true 已支付
    *
    **/
    private boolean isOrderPayed(OrderInfo order) {
        return order.getStatus() == OrderStatusConstant.ORDER_HAS_PAID;
    }

    /**
    * 订单是否已经取消
    *
    * @param order 订单
    * @return true 已取消
    *
    **/
    private boolean isOrderCanceled(OrderInfo order) {
        return order.getStatus() == OrderStatusConstant.ORDER_HAS_CANCELED;
    }

}
