package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
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

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private static final String CHAT_ENDPOINT = "/openChat";
    private static final String CHAT_ACCESS_ENDPOINT = "/chatChat";

    @GetMapping(value = CHAT_ENDPOINT)
    public String viewMyChat(Model model, ChatModel chatModel){
        logger.debug("openChat");

        model.addAttribute("chatForm", chatModel);

        return "userInfoPage";
    }

    @PostMapping(value = CHAT_ENDPOINT)
    public String openChat(Model model, Principal principal, ChatModel chatModel){
        return index(model, principal, chatModel);
    }



    @RequestMapping(CHAT_ACCESS_ENDPOINT)
    public String index(Model model, Principal principal, ChatModel chatModel) {

        String name = principal.getName();
        model.addAttribute("username", name);

        String chatName = chatModel.getChatName();
        model.addAttribute("chatName", chatName);

        String chatId = chatModel.getId();

        if (chatId == null){
            chatId = chatModel.getChatName()+chatModel.getChatDateOfCreation()+chatModel.getChatNameOfCreator();
        }

        List<ChatModel> check = api.readAllWhereSomething(ChatModel.class, chatId, "chat_id");
        if (check.size() != 0){
            for (ChatModel value : check) {
                if (!value.getId().equals(chatId)) {
                    return "redirect:/userInfo";
                }
            }
        } else {
            return "redirect:/userInfo";
        }

        model.addAttribute("chatId", chatId);

        List<ChatMessage> list = dao.findAllContentForThisChat(chatId);
        model.addAttribute("infoAboutOldMessages", list);

        List<ChatModel> participants = dao.findAllParticipants(chatId);
        model.addAttribute("participants", participants);


        return "chatChat";
    }

}
