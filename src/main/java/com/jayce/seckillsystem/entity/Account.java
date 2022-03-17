package com.jayce.seckillsystem.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "user")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户姓名
     */
    @TableField(value = "realname")
    private String username;
    /**
     * 密码
     */
    @TableField(value = "pwd")
    private String password;

    /**
     * 用户手机号
     */
    @TableField(value = "mobile_phone")
    private String mobilePhone;

    /**
     * 身份证号
     */
    @TableField(value = "identity_id")
    private String identityId;
}
