package com.example.listener;

import com.example.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;
import java.util.Objects;

@Component
    public class WebSocketEventListener {

        private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

        @Autowired
        private SimpMessageSendingOperations messagingTemplate;

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event) {
            logger.info("Received a new web socket connection");

            logger.info("User connect: " + Objects.requireNonNull(event.getUser()).getName());

            /*String nameOfListener = Objects.requireNonNull(event.getUser()).getName();
            logger.info("New user name event.getUser(): " + nameOfListener);

            messagingTemplate.convertAndSend("/topic/"+ "{chatName}" +"Room", nameOfListener);*/
        }

        @EventListener
        public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

            String username = (String) headerAccessor.getSessionAttributes().get("username");
            String chatName = (String) headerAccessor.getSessionAttributes().get("chatName");
            String chatId = (String) headerAccessor.getSessionAttributes().get("chatId");

            logger.info("chatName: " + chatName);
            logger.info("chatId:" + chatId);

            Date date =new Date();
            System.out.println(date);
            if(username != null) {
                logger.info("User Disconnected : " + username);

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(ChatMessage.MessageType.LEAVE);
                chatMessage.setSender(username);
                chatMessage.setChatName(chatName);
                chatMessage.setChatId(chatId);

                messagingTemplate.convertAndSend("/topic/"+ chatId +"Room", chatMessage);
            }
        }

}
