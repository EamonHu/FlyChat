package com.iflytek.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ymhu5
 * @Description: 服务器发给浏览器的数据
 * @Date: 2021/8/5 23:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessage {

    private boolean isSystem;
    private String fromName;
    /**
     * 如果是系统消息，则是个数组
     */
    private Object message;
}
