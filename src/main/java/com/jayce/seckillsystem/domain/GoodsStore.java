package com.jayce.seckillsystem.domain;

import com.jayce.seckillsystem.controller.SeckillController;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 商品库存信息
 *
 * @author <a href="mailto:su_1999@126.com">sujian</a>
 */
@Setter
@Getter
public class GoodsStore {
    /**
     * 记录某商品是否卖完了
     * <p>
     * 这里不使用 final 修饰的原因是商品数量不确定，无法指定初始化容量。
     * 在 {@link SeckillController}中的{@code goodsStockWarmUp()}中初始化。
     * 初始化后不要修改该变量
     */
    public static Map<Integer, Boolean> goodsSoldOut;


}
