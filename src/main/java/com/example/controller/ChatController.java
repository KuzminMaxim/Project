package com.example.controller;

import com.example.form.ChatForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class ChatController {

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

        System.out.println("chatName in chatChat: " + chatName);

        model.addAttribute("chatName", chatName);

        return "chatChat";
    }


    @RequestMapping("/chatChat1")
    public String index1(Model model, Principal principal) {

        String name = principal.getName();
        model.addAttribute("username", name);

        String chatName = "Event 123";
        model.addAttribute("chatName", chatName);

        return "chatChat";
    }

}
