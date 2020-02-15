package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.dao.ChatDAO;
import com.example.dao.UserDAO;
import com.example.model.ChatMessage;
import com.example.model.ChatModel;
import com.example.model.EventModel;
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
    UserDAO userDAO;

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

        String username = principal.getName();
        model.addAttribute("username", username);

        String chatId = chatModel.getId();

        ChatModel check = api.readSomethingOne(ChatModel.class, chatId, "chat_id");

        try {
            if (!check.getId().equals(chatId)){
                return "redirect:/userInfo";
            }
        } catch (NullPointerException ignored){
            return "redirect:/userInfo";
        }

        List<ChatModel> currentChat =
                api.readAllWhereSomething(ChatModel.class, chatId, "chat_id");

        if (currentChat.size() == 0){
            List<EventModel> currentEvent =
                    api.readAllWhereSomething(EventModel.class, Integer.toString(Integer.parseInt(chatId) - 1), "event_id");
            for (EventModel current : currentEvent){
                model.addAttribute("chatName", current.getNameOfEvent() + " (" + current.getEventStatus() + ")");
            }
        } else {
            for (ChatModel current : currentChat){
                model.addAttribute("chatName", current.getChatName() + " (" + current.getChatStatus() + ")");
            }
        }

        List<ChatModel> checkParticipant = dao.findAllParticipants(chatId);
        boolean success = false;
        for (ChatModel value : checkParticipant){
            if (value.getChatParticipant().equals(principal.getName())){
                success = true;
            }
        }
        if (!success){
            logger.info("The user {} tried to access the chat {}, in which he does not participate.", username, chatId);
            return "redirect:/userInfo";
        }

        model.addAttribute("chatId", chatId);

        List<ChatMessage> list = dao.findAllContentForThisChat(chatId);
        model.addAttribute("infoAboutOldMessages", list);

        List<ChatModel> participants = dao.findAllParticipants(chatId);
        for (ChatModel participant : participants) {
            String userAvatar = userDAO.getAvatarPath(participant.getChatParticipant());
            if (userAvatar != null) {
                participant.setUserAvatar(userAvatar.substring(40));
            } else {
                participant.setUserAvatar("\\uploads\\defaultAvatar.png");
            }
        }
        model.addAttribute("participants", participants);


        return "chatChat";
    }

}
