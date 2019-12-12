package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@RequestMapping
@Controller
public class WebSocketController {

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    public static List<String> usersOnlineList = new ArrayList<>();

    @MessageMapping("/{chatName}.sendMessage")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        api.save(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addUser")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        logger.info("User {} has been added in chat {}" , chatMessage.getSender() , chatMessage.getChatName());
        usersOnlineList.add(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("usersInOnlineList", usersOnlineList);

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addInOnline")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUserInOnlineList(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        return chatMessage;
    }

}