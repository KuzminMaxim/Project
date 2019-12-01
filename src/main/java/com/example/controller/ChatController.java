package com.example.controller;

import com.example.dao.ChatDAO;
import com.example.form.ChatForm;
import com.example.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired
    ChatDAO dao;

    @RequestMapping(value = "/openChat", method = RequestMethod.GET)
    public String viewMyChat(Model model, ChatForm chatForm){

        model.addAttribute("chatForm", chatForm);

        return "userInfoPage";
    }

    @RequestMapping(value = "/openChat", method = RequestMethod.POST)
    public String openChat(Model model, Principal principal, ChatForm chatForm){

        return index(model, principal, chatForm);
    }



    @RequestMapping("/chatChat")
    public String index(Model model, Principal principal, ChatForm chatForm) {

        String name = principal.getName();
        model.addAttribute("username", name);

        String chatName = chatForm.getChatName();
        model.addAttribute("chatName", chatName);

        List<ChatMessage> list = dao.findAllContentForThisChat(chatName);
        model.addAttribute("infoAboutOldMessages", list);

        return "chatChat";
    }

}
