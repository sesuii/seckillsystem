package com.jayce.seckillsystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayce.seckillsystem.dao.AccessRuleMapper;
import com.jayce.seckillsystem.entity.*;
import com.jayce.seckillsystem.entity.vo.GoodsVo;
import com.jayce.seckillsystem.service.IAccessRuleService;
import com.jayce.seckillsystem.service.IGoodsService;
import com.jayce.seckillsystem.service.IUserFinancialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * <p>
 * 准入规则表 服务实现类
 * </p>
 *
 * @author Gerry
 * @since 2022-04-13
 */
@Service
public class AccessRuleServiceImpl extends ServiceImpl<AccessRuleMapper, AccessRule> implements IAccessRuleService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IAccessRuleService accessRuleService;

    @Resource
    private IUserFinancialService userFinancialService;


    /**
    * @Description 判断用户是否有购买产品的资格
    *        根据准入规则的年龄，逾期次数和逾期金额限制
    * @param user 用户
    * @param goodsId 产品ID
    * @return
    *
    **/
    @Override
    public boolean checkAccessAuthority(User user, Long goodsId) {
        GoodsVo goodsVo = goodsService.findGoodsVoById(goodsId);
        Long limitedId = goodsVo.getLimitedRuleId();
        UserFinancial userFinancial = userFinancialService.getById(user.getId());
        if(limitedId != 0L) {
            AccessRule accessRule = accessRuleService.getById(limitedId);
            if(userFinancial.getIntegrityDegree() < 0) return false;
            // 用户无工作
            if(accessRule.getWorkstatusLimit() == 0) return false;
            if(!accessRule.getOverTotalAmount().equals(-1) ||
                    userFinancial.getOverAmount().compareTo(accessRule.getOverTotalAmount()) > 0)
                return false;
            return accessRule.getAgeLimit() == -1 &&
                    getUserAge(user.getIdentityId()) >= accessRule.getAgeLimit();
        }
        return true;
    }

    /**
    * @Description 从身份证中获取用户年龄
    *
    * @param identityId 身份证号
    * @return
    *
    **/
    private int getUserAge(String identityId) {
        String birthday = identityId.substring(6, 14);
        Calendar curCalendar = Calendar.getInstance();

        int curYear = curCalendar.get(Calendar.YEAR);
        int curMonth = curCalendar.get(Calendar.MONTH) + 1;
        int curDay = curCalendar.get(Calendar.DAY_OF_MONTH);

        int birthYear = Integer.parseInt(birthday.substring(0, 4));
        int birthMonth = Integer.parseInt(birthday.substring(4, 6));
        int birthDay = Integer.parseInt(birthday.substring(6));

        int userAge = curYear - birthYear;
        if(curMonth - birthMonth > 0 || (curMonth == birthMonth && curDay - birthDay > 0)) {
            return userAge;
        }
        return userAge - 1;
    }
}
