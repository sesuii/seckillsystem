package com.jayce.seckillsystem.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * @Description: 用户导入类
 * @DATA: 2022/3/24 22:34
 * @Author: YoungSong
 * @File: seckillsystem UserVo
 * @Email: sjiahui27@gmail.com
 **/


@Data
@AllArgsConstructor
public class UserVo {
    @NotNull
    private String mobile;

    @NotNull
    private String password;
}
