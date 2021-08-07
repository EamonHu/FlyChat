package com.iflytek.chat.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ymhu5
 * @Description: 用户拦截类
 * @Date: 2021/8/6 1:09
 */
@Component
@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    public static Map<String, String> onLineUsers = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession httpSession = request.getSession();
        String username = (String) httpSession.getAttribute("user");
        log.info("进入拦截器"+"==="+"进入拦截器的用户是："+username);
        if(username != null && !onLineUsers.containsKey(username)){
            onLineUsers.put(username,username);
            log.info("已存储的用户："+onLineUsers);
            return true;
        }else {
            log.warn("未授权用户");
            httpSession.removeAttribute("user");
            response.sendRedirect("/loginerror");
            return false;
        }
    }
}
