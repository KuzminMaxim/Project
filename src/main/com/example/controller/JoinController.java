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

@RequestMapping
@Controller
public class JoinController {

    @Autowired
    private ApiForInteractingWithTheDatabase apiForInteractingWithTheDatabase;

    @PostMapping(value = "/joinToEvent")
    public String CreateUser(EventModel eventModel, Principal principal, Model model, ChatModel chatModel) {
        if (eventModel.getNameOfEventCreator().equals(principal.getName())){
            String errorOfJoin = "You is creator of this event!";
            model.addAttribute("errorOfJoin", errorOfJoin);
            return "MyGoogleMap";
        } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator());
            eventModel.setId(eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator());
            chatModel.setId(eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator());
            apiForInteractingWithTheDatabase.add(eventModel);
            apiForInteractingWithTheDatabase.add(chatModel);
        }
        return "MyGoogleMap";
    }

}
