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

        List<EventModel> eventsWhereParticipant = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_participant");

        for (EventModel participant : eventsWhereParticipant) {

            String id = participant.getNameOfEvent() +
                    participant.getEventDateOfCreation() + participant.getNameOfEventCreator();

            String chatId = eventModel.getNameOfEvent() +
                    eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator();

            participant.setId(id);
            chatModel.setId(chatId);

            if (participant.getId().equals(chatId)) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }

        List<EventModel> eventsWhereCreator = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_name_of_creator");

        for (EventModel participant : eventsWhereCreator) {

            String id = participant.getNameOfEvent() +
                    participant.getEventDateOfCreation() + participant.getNameOfEventCreator();

            String chatId = eventModel.getNameOfEvent() +
                    eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator();

            participant.setId(id);
            chatModel.setId(chatId);

            if (participant.getId().equals(chatId)) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }

            eventModel.setId(eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator());
            chatModel.setId(eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator());
            api.add(eventModel);
            api.add(chatModel);

        return "redirect:/userInfo";
    }

}
