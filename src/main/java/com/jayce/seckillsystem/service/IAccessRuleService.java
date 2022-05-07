package com.jayce.seckillsystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayce.seckillsystem.entity.AccessRule;
import com.jayce.seckillsystem.entity.User;

/**
 * <p>
 * 准入规则表 服务类
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
public interface IAccessRuleService extends IService<AccessRule> {

    /**
    * 检查用户购买权限
    *
    * @param user 用户
    * @param goodsId 商品 ID
    * @return
    *
    **/
    boolean checkAccessAuthority(User user, Long goodsId);
}
