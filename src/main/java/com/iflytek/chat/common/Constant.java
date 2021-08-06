package com.iflytek.chat.common;

import com.iflytek.chat.pojo.User;
import com.iflytek.chat.websocket.ChatServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Constant
 * @Description 常量
 * @Author ymhu5
 * @Date 2021/8/6 14:30
 * @Version 1.0
 */
public class Constant {

    /**
     * 存储在session中的属性
     */
    public static final String USER = "user";

    /**
     * 聊天室所有的用户集合
     */
    public static Map<String, User> allUserMap = new ConcurrentHashMap<>();

    /**
     * 用来存储每一个客户端对象对应的ChatServer对象
     */
    public static Map<String, ChatServer> onLineUsers = new ConcurrentHashMap<>();
}
