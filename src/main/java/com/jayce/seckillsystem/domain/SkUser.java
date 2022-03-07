package com.jayce.seckillsystem.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 参与秒杀的用户
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sk_user")
public class SkUser implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    private static final long serialVersionUID = 1L;

    public static SkUserBuilder builder() {
        return new SkUserBuilder();
    }
}