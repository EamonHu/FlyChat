package com.iflytek.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: ymhu5
 * @Description: 配置类:注入ServerEndpointExporter这个对象
 * @Date: 2021/8/6 1:10
 */
@Configuration
public class WebSocketConfig {

    /**
     * 这个对象可以自动注册使用 @ServerEndpoint注解的bean 即本项目中的ChatEndPoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
