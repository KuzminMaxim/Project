package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatModel;
import com.example.model.EventModel;
import com.example.service.EventService;
import com.example.validation.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    @PostMapping(value = "/completeEvent")
    public String completeEvent(EventModel eventModel){

        eventService.preparingCompleteEvent(eventModel);

        return "redirect:/userInfo";
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
    public String createEvent(Model model, @RequestParam(required = false) String id) {
        logger.debug("Create event, method = GET");

        eventService.preparingGetEvent(id, model);

        return "MyGoogleMap";
    }

    @PostMapping(value = "/createEvent")
    public String createNewEvent(EventModel eventModel,
                                 ChatModel chatModel, Principal principal, Model model) {

        if (!validateNameOfEvent(eventModel.getNameOfEvent())){
            model.addAttribute("eventInfo", "Name of event contains invalid characters");
            return "error";
        }

                eventService.preparingPostEvent(eventModel, chatModel, principal);


        return "redirect:/userInfo";
    }

    private boolean validateNameOfEvent(String name){
        return name.matches("^[A-Za-z]+(?:(?: +|, |-)[A-Za-z]+)*$");
    }

}
