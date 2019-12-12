package com.example.controller;

import com.example.dao.ChatDAO;
import com.example.model.ChatMessage;
import com.example.model.ChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping
@Controller
public class ChatController {

    @Autowired
    ChatDAO dao;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @GetMapping(value = "/openChat")
    public String viewMyChat(Model model, ChatModel chatModel){
        logger.debug("openChat");

        model.addAttribute("chatForm", chatModel);

        return "userInfoPage";
    }

    @PostMapping(value = "/openChat")
    public String openChat(Model model, Principal principal, ChatModel chatModel){

        return index(model, principal, chatModel);
    }



    @RequestMapping("/chatChat")
    public String index(Model model, Principal principal, ChatModel chatModel) {


        String name = principal.getName();
        model.addAttribute("username", name);

        String chatName = chatModel.getChatName();
        model.addAttribute("chatName", chatName);

        List<ChatMessage> list = dao.findAllContentForThisChat(chatName);
        model.addAttribute("infoAboutOldMessages", list);

        List<ChatModel> participants = dao.findAllParticipants(chatName);
        model.addAttribute("participants", participants);


        return "chatChat";
    }

}
