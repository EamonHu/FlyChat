package com.iflytek.chat.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iflytek.chat.common.Constant;
import com.iflytek.chat.common.UserInterceptor;
import com.iflytek.chat.pojo.Message;
import com.iflytek.chat.pojo.User;
import com.iflytek.chat.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ymhu5
 * @Description: 实现客户端与服务端的交互，每一个client对应一个ChatServer
 * @Date: 2021/8/6 1:13
 */
@Slf4j
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatServer {

    private Session session;

    private HttpSession httpSession;

    public static Map<String, HashMap<String, ArrayList<ArrayList<String>>>> chatRecords = new ConcurrentHashMap<>();


    /**
     * 连接建立时被调用
     *
     * @param session webSocket中的Session
     * @param config  配置类，用于获取HttpSession
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String username = (String) httpSession.getAttribute(Constant.USER);
        log.info("上线用户名称：" + username);
        Constant.onLineUsers.put(username, this);
        String message = MessageUtils.getMessage(true, null, getNames());
        broadcastAllUsers(message);
    }

    /**
     * 收到客户端发送的数据时被调用
     *
     * @param message 收到的消息
     * @param session webSocket的session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message mess = mapper.readValue(message, Message.class);
            String toName = mess.getToName();
            String data = mess.getMessage();
            String username = (String) httpSession.getAttribute("user");
            log.info(username + "向" + toName + "发送的消息：" + data);

            ArrayList<ArrayList<String>> records;
            HashMap<String, ArrayList<ArrayList<String>>> someoneRecords = new HashMap<>();
            ArrayList<String> record = new ArrayList<>();
            record.add(username);
            record.add(data);
            if (chatRecords.containsKey(username) && chatRecords.get(username).containsKey(toName)) {
                records = chatRecords.get(username).get(toName);
            } else if (chatRecords.containsKey(toName) && chatRecords.get(toName).containsKey(username)) {
                records = chatRecords.get(toName).get(username);
            } else {
                records = new ArrayList<>();
            }
            records.add(record);
            someoneRecords.put(toName, records);
            chatRecords.put(username, someoneRecords);

            String resultMessage = MessageUtils.getMessage(false, username, data);
            if (StringUtils.hasLength(toName)) {
                Constant.onLineUsers.get(toName).session.getBasicRemote().sendText(resultMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭时被调用
     *
     * @param session webSocket的session
     */
    @OnClose
    public void onClose(Session session) {
        String username = (String) httpSession.getAttribute("user");
        log.info("离线用户：" + username);
        if (username != null) {
            Constant.onLineUsers.remove(username);
            UserInterceptor.onLineUsers.remove(username);
            User logoutUser = Constant.allUserMap.get(username);
            logoutUser.setStatus(false);
            Constant.allUserMap.put(username, logoutUser);
        }
        httpSession.removeAttribute("user");
        String message = MessageUtils.getMessage(true, null, getNames());
        broadcastAllUsers(message);
    }

    /**
     * 获取所有用户名：包含在线和不在线用户
     */
    private Map<String, User> getNames() {
        return Constant.allUserMap;
    }

    /**
     * 给所有用户广播消息
     */
    private void broadcastAllUsers(String message) {
        try {
            Set<String> names = Constant.onLineUsers.keySet();
            for (String name : names) {
                ChatServer chatServer = Constant.onLineUsers.get(name);
                chatServer.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取两个用户之间的历史聊天记录
     */
    public void getHistoryMessagesString(String fromUserName, String toUserName) {
        // 获取fromUseName和toUserName之间的聊天记录
        ArrayList<ArrayList<String>> records;
        if (ChatServer.chatRecords.containsKey(fromUserName) && ChatServer.chatRecords.get(fromUserName).containsKey(toUserName)) {
            records = ChatServer.chatRecords.get(fromUserName).get(toUserName);
        } else if (ChatServer.chatRecords.containsKey(toUserName) && ChatServer.chatRecords.get(toUserName).containsKey(fromUserName)) {
            records = ChatServer.chatRecords.get(toUserName).get(fromUserName);
        } else {
            return;
        }
        try {
            // 遍历两个人之间的聊天记录，并逐一发送
            for (ArrayList<String> singleRecord : records) {
                // 每条记录存储发送人姓名和发送内容
                if (singleRecord.get(0).equals(fromUserName)) {
                    String resultMessage = MessageUtils.getMessage(false, fromUserName, singleRecord.get(1));
                    Constant.onLineUsers.get(toUserName).session.getBasicRemote().sendText(resultMessage);
                } else {
                    String resultMessage = MessageUtils.getMessage(false, toUserName, singleRecord.get(1));
                    Constant.onLineUsers.get(fromUserName).session.getBasicRemote().sendText(resultMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
