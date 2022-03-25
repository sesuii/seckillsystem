package com.jayce.seckillsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author YoungSong
 * @since 2022-03-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  真实姓名
     */
    private String realname;

    /**
     *  密码
     */
    private String pwd;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 身份证号
     */
    private String identityId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *  最后一次修改时间
     */
    private Date lastUpdateTime;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


}
