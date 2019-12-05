package com.example.controller;

import com.example.api.MyApi;
import com.example.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class WebSocketController {

    @Autowired
    MyApi api;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/{chatName}.sendMessage")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws NoSuchFieldException, IllegalAccessException {

        logger.info("Type of message: " + chatMessage.getType().toString());
        logger.info("Content of message: " + chatMessage.getContent());
        api.save(chatMessage);
        logger.info("Message has been sent");

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addUser")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        logger.info("inAddUser name:", chatMessage.getSender());
        System.out.println(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        logger.info("User has been added: " + chatMessage.getSender() + " in chat: " + chatMessage.getChatName());

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addInOnline")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage removeUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        String user = chatMessage.getContent();
        String[] x = new String[100];
        for (int i = 0; i< x.length; i++){
            if (x[i] == null){
                x[i] = user;
                System.out.println(x[i]);
                break;
            }
        }
        System.out.println(Arrays.toString(x));


        System.out.println("user: " + user);

        List <String> chatMessages = new ArrayList<>();
        chatMessages.add("123");
        System.out.println("chatMessages.size(): " + chatMessages.size());

        if (!user.equals(chatMessages.get(0))) {
            chatMessages.add(chatMessage.getContent());
            System.out.println("@@@chatMessages.size(): " + chatMessages.size());
        }

        //System.out.println("@@@chatMessages.size(): " + chatMessages.size());

        /*List <ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);
        System.out.println("chatMessages.size(): " + chatMessages.size());
        System.out.println("inAddOnline: " + chatMessage.getSender());
        System.out.println("inAddOnline: " + chatMessage.getContent());*/


        /*List<String> usersOnline = new ArrayList<>();

        //for (int i = 0; i< )

        usersOnline.add(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("usersOnline",usersOnline);
        System.out.println(Arrays.toString(usersOnline.toArray()));*/



        /*headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        logger.info("User has been removed: " + chatMessage.getSender() + " in chat: " + chatMessage.getChatName());*/

        return chatMessage;
    }

}