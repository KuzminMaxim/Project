package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.EventModel;
import com.example.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping
@Controller
public class DeleteController {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(DeleteController.class);

    @GetMapping(value = "/deleteSomething")
    public String viewDeletePage(Model model) {

        logger.debug("Delete something, method GET");

        UserModel registrationForm = new UserModel();
        EventModel eventModel = new EventModel();

        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm", eventModel);

        return "deletePage";
    }

    @PostMapping(value = "/deleteSomething")
    public String deleteUser(UserModel registrationForm, EventModel eventModel) {

        logger.debug("Delete something, method POST");

        if (registrationForm.getName() != null){
            api.delete(registrationForm);
            logger.info("User {} was deleting", registrationForm.getName());
        }

        if (eventModel.getId() != null){
            List<EventModel> check = api.readAllWhereSomething(EventModel.class, eventModel.getId(), "event_id");
            if (check.size() != 0){
                for (EventModel value : check) {
                    if (!value.getId().equals(eventModel.getId())) {
                        return "redirect:/userInfo";
                    }
                }
            } else {
                return "redirect:/userInfo";
            }
            eventModel.setEventStatus("cancelled");
            api.update(eventModel);
            logger.info("Event {} was cancelled", eventModel.getNameOfEvent());
        }

        return "redirect:/userInfo";
    }

    @PostMapping(value = "/deleteParticipantFromEvent")
    public String deleteParticipantFromEvent(EventModel eventModel, Principal principal) {

        eventModel.setEventParticipant(principal.getName());

        logger.debug("Delete participant from event, method = POST");

        if (eventModel.getNameOfEvent() != null){

            api.deleteOne(eventModel);
            logger.info("User {} was deleting from event {}", eventModel.getEventParticipant(), eventModel.getId());
        }

        return "redirect:/userInfo";
    }

}
