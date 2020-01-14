package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.ChatModel;
import com.example.model.EventModel;
import com.example.model.UserModel;
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

        String linkToEvent = "<a href=\"" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                "/createEvent?latitude=" + eventModel.getLatitude() +
                "&longitude=" + eventModel.getLongitude() + "\">" + eventModel.getNameOfEvent() + "</a>";

        model.addAttribute("nameOfEventToLink", eventModel.getNameOfEvent());
        model.addAttribute("linkToEvent", linkToEvent);
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

        EventModel form = new EventModel();
        ChatModel chatModel = new ChatModel();
        model.addAttribute("eventModel", form);
        model.addAttribute("chatModel", chatModel);

        List<EventModel> testList;

        if (latitude == null){
            testList = apiForInteractingWithTheDatabase.readAll(EventModel.class);
        } else {
            testList = apiForInteractingWithTheDatabase
                    .readAllWhereSomething(EventModel.class, latitude, "event_lat");
        }

            String[] eventName = new String[testList.toArray().length];
            Double[] eventLat = new Double[testList.toArray().length];
            Double[] eventLng = new Double[testList.toArray().length];
            String[] eventDescription = new String[testList.toArray().length];
            String[] dates = new String[testList.toArray().length];
            String[] eventNameOfCreator = new String[testList.toArray().length];
            String[] dateOfCreation = new String[testList.toArray().length];
            int n = 0;
            for (int i = 0; i < testList.toArray().length; i++, n++){
                eventName[n] = testList.get(i).getNameOfEvent();
                eventLat[n] = Double.valueOf(testList.get(i).getLatitude());
                eventLng[n] = Double.valueOf(testList.get(i).getLongitude());
                eventDescription[n] = testList.get(i).getDescriptionOfEvent();
                dates[n] = testList.get(i).getDate().replace("T", " ");
                eventNameOfCreator[n] = testList.get(i).getNameOfEventCreator();
                dateOfCreation[n] = testList.get(i).getEventDateOfCreation();
            }
            model.addAttribute("eventName", eventName);
            model.addAttribute("eventLat", eventLat);
            model.addAttribute("eventLng", eventLng);
            model.addAttribute("eventDescript", eventDescription);
            model.addAttribute("eventDate", dates);
            model.addAttribute("eventNameOfCreator", eventNameOfCreator);
            model.addAttribute("dateOfCreation", dateOfCreation);

        return "MyGoogleMap";
    }

    @PostMapping(value = "/createEvent")
    public String createNewEvent(Model model, @ModelAttribute("eventForm") @Validated EventModel eventModel,
                             BindingResult result, ChatModel chatModel) {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String dateOfCreation = ts.toString();

        String newIdForEventAndChat = eventModel.getNameOfEvent() + dateOfCreation + eventModel.getNameOfEventCreator();

        logger.debug("Create event, method = POST");
        logger.info("result.hasErrors {}", result.hasErrors());
        if (result.hasErrors()) {
            List<EventModel> eventName = apiForInteractingWithTheDatabase.readAll(EventModel.class);
            model.addAttribute("eventInfo", eventName);
            return "MyGoogleMap";
        }
        try {

            eventModel.setId(newIdForEventAndChat);
            eventModel.setEventDateOfCreation(dateOfCreation);
            chatModel.setId(newIdForEventAndChat);
            chatModel.setChatDateOfCreation(dateOfCreation);

            apiForInteractingWithTheDatabase.save(eventModel);
            apiForInteractingWithTheDatabase.save(chatModel);
            logger.info("Event {} and chat {} was created", eventModel.getNameOfEvent(), chatModel.getChatName());

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
