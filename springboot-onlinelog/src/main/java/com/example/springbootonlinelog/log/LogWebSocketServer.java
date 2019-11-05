package com.example.springbootonlinelog.log;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * WebSocket端口
 */
@ServerEndpoint(value = "/logWebsocket")
@Component
public class LogWebSocketServer {

    // Session对应一个客户端
    private static final Set<Session> sessionSet = new HashSet<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionSet.add(session);
        sendMessage("----------------已建立连接等待日志信息---------------", session);
    }

    /**
     * 广播消息
     */
    public static void sendMessageToAll(String message) {
        sessionSet.forEach(session -> {
            sendMessage(message, session);
        });
    }

    /**
     * 发送给指定的人
     */
    private static void sendMessage(String message, Session session) {
        if(message == null || message.equals("") || session == null){
            return;
        }
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        // 删除session与socket对象
        sessionSet.remove(session);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
        // 删除session与socket对象
        sessionSet.remove(session);
    }
}
