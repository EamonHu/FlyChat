package com.iflytek.chat.controller;

import com.iflytek.chat.common.Constant;
import com.iflytek.chat.pojo.Result;
import com.iflytek.chat.pojo.User;
import com.iflytek.chat.websocket.ChatServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ymhu5
 * @Description: 用户登录
 * @Date: 2021/8/5 23:49
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private ChatServer chatServer;

    @RequestMapping("/toLogin")
    public Result toLogin(@RequestParam("user") String loginUser,
                          @RequestParam("pwd") String loginPwd,
                          HttpSession httpSession) {
        Result result = new Result();
        httpSession.setMaxInactiveInterval(3000 * 60);
        log.info(System.currentTimeMillis() + loginUser + "登录验证中..");
        if (httpSession.getAttribute(Constant.USER) != null) {
            result.setFlag(false);
            result.setMessage("不支持一个浏览器登录多个用户，换个浏览器登录吧~");
            return result;
        }
        // 判断登录用户的密码是否正确
        if (Constant.allUserMap.containsKey(loginUser)) {
            User mapUser = Constant.allUserMap.get(loginUser);
            if (!mapUser.getPassWord().equals(loginPwd)) {
                result.setFlag(false);
                log.warn(loginUser + "登录校验失败");
                result.setMessage("登录校验失败");
                return result;
            }
        } else {
            //第一次登陆新建用户
            User registerUser = new User();
            registerUser.setPassWord(loginPwd);
            registerUser.setUserName(loginUser);
            registerUser.setStatus(true);
            Constant.allUserMap.put(loginUser, registerUser);
        }
        result.setFlag(true);
        log.info(loginUser + "登录验证成功");
        httpSession.setAttribute(Constant.USER, loginUser);
        return result;
    }


    /**
     * 获取用户名
     * @return 用户名
     */
    @RequestMapping("/getUsername")
    public String getUsername(HttpSession httpSession) {
        return (String) httpSession.getAttribute("user");
    }


    /**
     * 获取用户间的历史聊天信息
     * @param toUsername 发送对象
     * @param httpSession webSocket的Session
     * @return Result
     */
    @RequestMapping("/getChatRecordsByName")
    public Result getChatRecordsByName(String toUsername, HttpSession httpSession) {
        String fromUser = getUsername(httpSession);
        chatServer.getHistoryMessagesString(fromUser, toUsername);
        Result result = new Result();
        result.setFlag(true);
        result.setMessage("调用获取历史信息接口成功");
        return result;
    }
}
