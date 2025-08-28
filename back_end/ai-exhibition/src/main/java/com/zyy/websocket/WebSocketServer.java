package com.zyy.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.util.Iterator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * WebSocket服务
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    //存放会话对象
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    
    //存放连接的最后心跳时间
    private static final Map<String, LocalDateTime> lastHeartbeatMap = new ConcurrentHashMap<>();
    
    //心跳间隔时间（秒）
    private static final int HEARTBEAT_INTERVAL = 30;
    
    //心跳超时时间（秒）
    private static final int HEARTBEAT_TIMEOUT = 90;
    
    //定时任务执行器
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    
    //心跳消息类型
    private static final String HEARTBEAT_TYPE = "heartbeat";
    private static final String HEARTBEAT_RESPONSE_TYPE = "heartbeat_response";
    
    static {
        // 启动心跳发送定时任务
        scheduler.scheduleAtFixedRate(() -> {
            sendHeartbeatToAll();
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
        
        // 启动连接检查定时任务
        scheduler.scheduleAtFixedRate(() -> {
            checkAndCleanTimeoutConnections();
        }, HEARTBEAT_TIMEOUT, HEARTBEAT_TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("客户端：{}建立连接", sid);
        sessionMap.put(sid, session);
        // 记录连接时间作为初始心跳时间
        lastHeartbeatMap.put(sid, LocalDateTime.now());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端：{}的信息:{}", sid, message);
        
        try {
            // 尝试解析JSON消息
            JSONObject jsonMessage = JSON.parseObject(message);
            String type = jsonMessage.getString("type");
            
            // 处理心跳响应消息
            if (HEARTBEAT_RESPONSE_TYPE.equals(type)) {
                // 更新最后心跳时间
                lastHeartbeatMap.put(sid, LocalDateTime.now());
                log.debug("收到客户端：{}的心跳响应", sid);
                return;
            }
            
            // 处理其他类型的消息
            // TODO: 在这里添加其他消息类型的处理逻辑
            
        } catch (Exception e) {
            // 如果不是JSON格式或解析失败，按普通消息处理
            log.debug("收到非JSON格式消息或解析失败：{}", message);
        }
    }

    /**
     * 连接关闭调用的方法
     * @param sid
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("连接断开:{}", sid);
        sessionMap.remove(sid);
        lastHeartbeatMap.remove(sid);
    }

    /**
     * 群发
     * @param message
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送消息失败: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 向所有客户端发送心跳消息
     */
    private static void sendHeartbeatToAll() {
        if (sessionMap.isEmpty()) {
            return;
        }
        
        JSONObject heartbeatMessage = new JSONObject();
        heartbeatMessage.put("type", HEARTBEAT_TYPE);
        heartbeatMessage.put("timestamp", System.currentTimeMillis());
        String message = heartbeatMessage.toJSONString();
        
        log.debug("发送心跳消息给{}个客户端", sessionMap.size());
        
        Iterator<Map.Entry<String, Session>> iterator = sessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            String sid = entry.getKey();
            Session session = entry.getValue();
            
            try {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                } else {
                    // 连接已关闭，清理资源
                    iterator.remove();
                    lastHeartbeatMap.remove(sid);
                    log.info("清理已关闭的连接：{}", sid);
                }
            } catch (Exception e) {
                log.error("发送心跳消息失败，客户端：{}，错误：{}", sid, e.getMessage());
                // 发送失败，清理连接
                iterator.remove();
                lastHeartbeatMap.remove(sid);
            }
        }
    }
    
    /**
     * 检查并清理超时的连接
     */
    private static void checkAndCleanTimeoutConnections() {
        if (lastHeartbeatMap.isEmpty()) {
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        Iterator<Map.Entry<String, LocalDateTime>> iterator = lastHeartbeatMap.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDateTime> entry = iterator.next();
            String sid = entry.getKey();
            LocalDateTime lastHeartbeat = entry.getValue();
            
            // 检查是否超时
            if (lastHeartbeat.plusSeconds(HEARTBEAT_TIMEOUT).isBefore(now)) {
                log.info("检测到超时连接，清理客户端：{}", sid);
                
                // 关闭连接
                Session session = sessionMap.get(sid);
                if (session != null) {
                    try {
                        session.close();
                    } catch (Exception e) {
                        log.error("关闭超时连接失败：{}", e.getMessage());
                    }
                }
                
                // 清理资源
                sessionMap.remove(sid);
                iterator.remove();
            }
        }
    }
    
    /**
     * 获取当前连接数
     */
    public static int getConnectionCount() {
        return sessionMap.size();
    }
    
    /**
     * 获取连接状态信息
     */
    public static Map<String, Object> getConnectionStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connectionCount", sessionMap.size());
        status.put("heartbeatInterval", HEARTBEAT_INTERVAL);
        status.put("heartbeatTimeout", HEARTBEAT_TIMEOUT);
        return status;
    }

}
