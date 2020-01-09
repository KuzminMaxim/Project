package com.example.listener;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatMessage;
import com.example.model.UserLogoutChatModel;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
    public class WebSocketEventListener {

        private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

        @Autowired
        private SimpMessageSendingOperations messagingTemplate;

        @Autowired
        private static Multimap<String, String> chatRatioWithUsers = ArrayListMultimap.create();

        @Autowired
        private ApiForInteractingWithTheDatabase api;

        @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

            logger.info("Received a new web socket connection");

            logger.info("User {} was connected", Objects.requireNonNull(headerAccessor.getUser()).getName());
        }

        @EventListener
        public void handleSessionSubscribeListener(SessionSubscribeEvent event){
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

            String destination = (String) event.getMessage().getHeaders().get("simpDestination");

            assert destination != null;

            String username =  Objects.requireNonNull(event.getUser()).getName();
            String chatId = destination.substring(7, destination.length() - 4);

            chatRatioWithUsers.put(chatId, username);

            logger.info("users in {} : {}", chatId, chatRatioWithUsers.get(chatId));

            Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("chatRatioWithUsers",chatRatioWithUsers);

            String[] usersOnline = chatRatioWithUsers.get(chatId).toArray(new String[0]);



            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.JOIN);
            chatMessage.setChatId(chatId);
            chatMessage.setUsersOnline(usersOnline);

            messagingTemplate.convertAndSend("/topic/"+ chatId +"Room", chatMessage);

        }

        @EventListener
        public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

            String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
            String chatName = (String) headerAccessor.getSessionAttributes().get("chatName");
            String chatId = (String) headerAccessor.getSessionAttributes().get("chatId");

            Date date = new Date();
            System.out.println(date);
            if(username != null) {
                logger.info("User {} was disconnected from chat {}", username, chatId);
                chatRatioWithUsers.remove(chatId, username);
                String[] usersOnline = chatRatioWithUsers.get(chatId).toArray(new String[0]);

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setType(ChatMessage.MessageType.LEAVE);
                chatMessage.setSender(username);
                chatMessage.setChatName(chatName);
                chatMessage.setChatId(chatId);
                chatMessage.setUsersOnline(usersOnline);

                UserLogoutChatModel logoutChatModel = new UserLogoutChatModel();

                logoutChatModel.setChatId(chatMessage.getChatId());
                logoutChatModel.setUserName(chatMessage.getSender());
                Timestamp ts = new Timestamp(date.getTime());
                String currentDate = ts.toString();

                if (currentDate.length() == 22){
                    currentDate  = currentDate + "0";
                }
                else if (currentDate.length() == 21){
                    currentDate  = currentDate + "00";
                }
                else if (currentDate.length() == 20){
                    currentDate  = currentDate + "000";
                }

                logoutChatModel.setUserLogoutTime(currentDate);

                List<UserLogoutChatModel> list = api.readAllWhereSomething(UserLogoutChatModel.class, username, "logout_user_name");

                if (list.size() == 0){
                    api.add(logoutChatModel);
                } else {
                    boolean check = true;
                    for (UserLogoutChatModel userLogoutChatModel : list) {
                        if (userLogoutChatModel.getChatId().equals(chatId)){
                            if (userLogoutChatModel.getUserName().equals(username)){
                                api.update(logoutChatModel);
                                check = false;
                                break;
                            }
                        }
                    }
                    if (check){
                        api.add(logoutChatModel);
                    }
                }

                messagingTemplate.convertAndSend("/topic/"+ chatId +"Room", chatMessage);
            }
        }

}
