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
import org.springframework.ui.Model;

import java.util.*;

@Controller
public class WebSocketController {

    @Autowired
    MyApi api;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    public static List<String> usersOnlineList = new ArrayList<>();
    //public static Map<String, String> usersOnlineMap = new HashMap<>();
    //public static List<Map<String, String>> usersOnlineList = new ArrayList<>();

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

        logger.info("inAddUser name:" + chatMessage.getSender());
        System.out.println(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatName", chatMessage.getChatName());
        logger.info("User " + chatMessage.getSender() + " has been added in chat: " + chatMessage.getChatName());
        usersOnlineList.add(chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("usersInOnlineList", usersOnlineList);
        //model.addAttribute("usersInOnlineList", usersOnlineList);

        //System.out.println("usersOnlineMap.size(): " + usersOnlineMap.size());
        //System.out.println("usersOnlineList.size(): " + usersOnlineList.size());
        //usersOnlineMap.add(chatMessage.getSender());
        //usersOnlineMap.put(chatMessage.getChatName(), chatMessage.getSender());


        /*for (int i = 0; i<usersOnlineList.size(); i++){
            System.out.println("UserNameInList: " + usersOnlineList.get(i));
        }*/

        //System.out.println("usersOnlineList.size() after add: " + usersOnlineList.size());

        return chatMessage;
    }

    @MessageMapping("/{chatName}.addInOnline")
    @SendTo("/topic/{chatName}Room")
    public ChatMessage addUserInOnlineList(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {


        /*System.out.println("inAddOnline: " + chatMessage.getSender());
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