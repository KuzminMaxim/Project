package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatModel;
import com.example.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping
@Controller
public class JoinController {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    @Autowired
    ChatController chatController;

    @PostMapping(value = "/joinToEvent")
    public String joinToEvent(EventModel eventModel, Principal principal, Model model, ChatModel chatModel) {

        String eventParticipant = principal.getName();
        eventModel.setEventParticipant(eventParticipant);
        chatModel.setChatParticipant(eventParticipant);
        chatModel.setId(Integer.toString(Integer.parseInt(eventModel.getId()) + 1));

        System.out.println("!!! + chatModel.getId() + !!!:" + chatModel.getId());

        List<EventModel> eventsWhereParticipant = api.readAllWhereSomething(EventModel.class, eventParticipant, "event_participant");

        for (EventModel participant : eventsWhereParticipant) {


            System.out.println("participant.getId(): " + participant.getId());
            System.out.println("chatModel.getId(): " + chatModel.getId());

            if (Integer.toString(Integer.parseInt(participant.getId()) + 1).equals(chatModel.getId())) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }

        List<EventModel> eventsWhereCreator = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_name_of_creator");

        for (EventModel participant : eventsWhereCreator) {

            System.out.println("creator.getId(): " + participant.getId());
            System.out.println("chatModel.getId(): " + chatModel.getId());


            if (Integer.toString(Integer.parseInt(participant.getId()) + 1).equals(chatModel.getId())) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }


            api.add(eventModel);
            api.add(chatModel);

        return "redirect:/userInfo";
    }

}
