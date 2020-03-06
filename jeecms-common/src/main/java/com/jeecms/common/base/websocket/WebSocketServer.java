//package com.jeecms.common.base.websocket;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.ConcurrentHashMap;
//
////@ServerEndpoint("/webSocket/{loginUser}")
//@Component
//public class WebSocketServer {
//    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
//    private Session session;
//    private Process process;
//    private InputStream inputStream;
//    private static ConcurrentHashMap<String, WebSocketServer> webSocketConcurrentHashMap = new ConcurrentHashMap<String, WebSocketServer>();
//    private String loginUser;
//    
//    @OnOpen
//    public void onOpen(@PathParam(value = "loginUser")String loginUser,Session session){
//        this.session=session;
//        this.loginUser = loginUser;
//        webSocketConcurrentHashMap.put(loginUser,this);
//        log.info("【websocket消息】 有新的连接，总数：{}",webSocketConcurrentHashMap.size());
//    }
//
//    @OnClose
//    public void onClose(){
//        webSocketConcurrentHashMap.remove(loginUser);
//    log.info("【websocket消息】 连接断开，总数：{}",webSocketConcurrentHashMap.size());
//    try {
//        if(inputStream != null) inputStream.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    if (process != null)
//        process.destroy();
//    }
//    @OnMessage
//    public void onMessage(String message){
//        log.info("【websocket消息】 收到客户端发来的消息：{}",message);
//    }
//
//    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
//    }
//
//    public void sendMessageToUser(String message){
//        String loginUser = message.split(WebSocketContext.MESSAGE_PARTITION_ELEMENT)[0];
//        message = message.split(WebSocketContext.MESSAGE_PARTITION_ELEMENT)[1];
//        try {
//            if (webSocketConcurrentHashMap.get(loginUser) != null) {
//                webSocketConcurrentHashMap.get(loginUser).sendMessage(message);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}