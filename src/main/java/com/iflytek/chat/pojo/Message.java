package com.iflytek.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ymhu5
 * @Description: 浏览器发给服务器的数据
 * @Date: 2021/8/5 23:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String toName;
    private String message;
}
