package com.example.controller;

import com.example.api.MyApi;
import com.example.dao.NewEventDAO;
import com.example.form.ChatForm;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
import com.example.model.EventInfo;
import com.example.model.UserInfo;
import com.example.validation.EventValidator;
import com.example.validation.UserValidator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private NewEventDAO eventDAO;

    @Autowired
    private MyApi myApi;

    @Autowired
    private EventValidator eventValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == EventForm.class) {
            dataBinder.setValidator(eventValidator);
        }
    }


    @RequestMapping(value = "/viewAllEvents", method = RequestMethod.GET)
    public String showEvents(Model model) {
        List<EventInfo> list = eventDAO.getAllEvents();
        model.addAttribute("eventInfo", list);
        return "eventPage";
    }

    @RequestMapping(value = "/viewAllEventMarkers", method = RequestMethod.GET)
    public String showEventMarkers(Model model) {
        List<EventInfo> list = eventDAO.getAllEventMarkers();
        List<EventInfo> testList = eventDAO.getAllEventMarkers();
        System.out.println(testList.get(0).getNameOfEvent());
        System.out.println(list);
        model.addAttribute("oldMarkers", list);
        return "viewEvents";
    }

    @RequestMapping(value = "/createEvent", method = RequestMethod.GET)
    public String createEvent(Model model) {
        EventForm form = new EventForm();
        ChatForm chatForm = new ChatForm();
        model.addAttribute("eventForm", form);
        model.addAttribute("chatForm", chatForm);
        List<EventInfo> testList = eventDAO.getAllEventMarkers();
        String[] eventName = new String[testList.toArray().length];
        Double[] eventLat = new Double[testList.toArray().length];
        Double[] eventLng = new Double[testList.toArray().length];
        String[] eventDescription = new String[testList.toArray().length];
        String[] dates = new String[testList.toArray().length];
        String[] eventNameOfCreator = new String[testList.toArray().length];
        int n = 0;
        for (int i = 0; i<testList.toArray().length; i++, n++){
            eventName[n] = testList.get(i).getNameOfEvent();
            eventLat[n] = testList.get(i).getEventLatitude();
            eventLng[n] = testList.get(i).getEventLongitude();
            eventDescription[n] = testList.get(i).getEventDescription();
            dates[n] = testList.get(i).getDate().replace("T", " ");
            eventNameOfCreator[n] = testList.get(i).getNameOfEventCreator();
        }
        model.addAttribute("eventName", eventName);
        model.addAttribute("eventLat", eventLat);
        model.addAttribute("eventLng", eventLng);
        model.addAttribute("eventDescript", eventDescription);
        model.addAttribute("eventDate", dates);
        model.addAttribute("eventNameOfCreator", eventNameOfCreator);
        return "MyGoogleMap";
    }

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public String createUser(Model model, @ModelAttribute("eventForm") @Validated EventForm eventForm,
                             BindingResult result, ChatForm chatForm) {

        if (result.hasErrors()) {
            List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            return "redirect:/createEvent";
        }
        try {
            myApi.save(eventForm);
            myApi.save(chatForm);
        } catch (NoSuchFieldException | IllegalAccessException e){
            List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/createEvent";
        }
        return "redirect:/userInfo";
    }

}
