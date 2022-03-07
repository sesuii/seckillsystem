package com.jayce.seckillsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 进入消息队列中的消息体
 *
 * @author <a href="mailto: su_1999@126.com">sujian</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkMessage {
    private Integer userId;

    private Integer goodsId;
}
