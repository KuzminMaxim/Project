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

        List<EventModel> eventStatus = api.readAllWhereSomething(EventModel.class, eventModel.getId(), "event_id");

        if (eventStatus.size() == 0){
            model.addAttribute("eventInfo", "This event does not exist!");
            return "error";
        }

        for (EventModel status : eventStatus){
            if (!status.getEventStatus().equals("active")){
                model.addAttribute("eventInfo", "This event does not active!");
                return "error";
            }
        }

        List<EventModel> eventsWhereParticipant = api.readAllWhereSomething(EventModel.class, eventParticipant, "event_participant");

        for (EventModel participant : eventsWhereParticipant) {

            if (Integer.toString(Integer.parseInt(participant.getId()) + 1).equals(chatModel.getId())) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }

        List<EventModel> eventsWhereCreator = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_name_of_creator");

        for (EventModel participant : eventsWhereCreator) {

            if (Integer.toString(Integer.parseInt(participant.getId()) + 1).equals(chatModel.getId())) {
                 return chatController.openChat(model, principal, chatModel);
            }
        }


            api.add(eventModel);
            api.add(chatModel);

        return "redirect:/userInfo";
    }

}
