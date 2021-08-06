package com.iflytek.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ymhu5
 * @Description: 登录响应返回给浏览器数据
 * @Date: 2021/8/5 23:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private boolean flag;
    private String message;
}
