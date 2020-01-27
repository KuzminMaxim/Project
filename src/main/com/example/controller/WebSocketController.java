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

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@RequestMapping
@Controller
public class WebSocketController {

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @MessageMapping("/{chatId}.sendMessage")
    @SendTo("/topic/{chatId}Room")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        Date date = new Date();
        Timestamp currentDate = new Timestamp(date.getTime());
        chatMessage.setCurrentDate(currentDate.toString());

        api.save(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/{chatId}.addUser")
    @SendTo("/topic/{chatId}Room")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        headerAccessor.getSessionAttributes().put("chatId", chatMessage.getChatId());

        logger.info("User {} has been added in chat {}, chatId: {}" , chatMessage.getSender() , chatMessage.getChatName(), chatMessage.getChatId());


        return chatMessage;
    }

    @MessageMapping("/{chatId}.addInOnline")
    @SendTo("/topic/{chatId}Room")
    public ChatMessage addUserInOnlineList(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        return chatMessage;
    }

}