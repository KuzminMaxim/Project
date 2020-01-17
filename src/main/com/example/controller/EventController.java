package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatModel;
import com.example.model.EventModel;
import com.example.model.UserModel;
import com.example.service.EventService;
import com.example.validation.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping
@Controller
public class EventController {

    @Autowired
    private ApiForInteractingWithTheDatabase apiForInteractingWithTheDatabase;

    @Autowired
    private EventValidator eventValidator;

    @Autowired
    private EventService eventService;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }

        if (target.getClass() == EventModel.class) {
            dataBinder.setValidator(eventValidator);
        }
    }

    @PostMapping(value = "/getLinkToEvent")
    public String getLinkToEvent(Model model, EventModel eventModel, HttpServletRequest request){

        eventService.preparingLinkToEvent(model, eventModel, request);

        return "userInfoPage";
    }


    @GetMapping(value = "/viewAllEvents")
    public String showEvents(Model model) {
        logger.debug("View all events, method = GET");

        List<EventModel> list = apiForInteractingWithTheDatabase.readAll(EventModel.class);
        model.addAttribute("allEvents", list);
        return "eventPage";
    }

    @GetMapping(value = "/createEvent")
    public String createEvent(Model model, @RequestParam(required = false) String latitude) {
        logger.debug("Create event, method = GET");

        eventService.preparingGetEvent(latitude, model);

        return "MyGoogleMap";
    }

    @PostMapping(value = "/createEvent")
    public String createNewEvent(Model model, @ModelAttribute("eventForm") @Validated EventModel eventModel,
                             BindingResult result, ChatModel chatModel) {

        if (result.hasErrors()) {
            eventService.resultHasErrors(model);
            return "MyGoogleMap";
        }
        try {
            eventService.preparingPostEvent(eventModel, chatModel);
        } catch (Exception e){

            logger.error("Unknown error", e);
            List<EventModel> eventName = apiForInteractingWithTheDatabase.readAll(EventModel.class);
            model.addAttribute("eventInfo", eventName);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());

            return "/createEvent";

        }
        return "redirect:/userInfo";
    }

}
