package com.example.controller;

import com.example.dao.NewEventDAO;
import com.example.form.EventForm;
import com.example.model.EventInfo;
import com.example.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private NewEventDAO eventDAO;


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
        model.addAttribute("eventForm", form);
        List<EventInfo> testList = eventDAO.getAllEventMarkers();
        String[] eventName = new String[testList.toArray().length];
        Double[] eventLat = new Double[testList.toArray().length];
        Double[] eventLng = new Double[testList.toArray().length];
        String[] eventDescription = new String[testList.toArray().length];
        int n = 0;
        for (int i = 0; i<testList.toArray().length; i++, n++){
            eventName[n] = testList.get(i).getNameOfEvent();
            eventLat[n] = testList.get(i).getEventLatitude();
            eventLng[n] = testList.get(i).getEventLongitude();
            eventDescription[n] = testList.get(i).getEventDescription();
        }
        model.addAttribute("eventName", eventName);
        model.addAttribute("eventLat", eventLat);
        model.addAttribute("eventLng", eventLng);
        model.addAttribute("eventDescript", eventDescription);
        return "MyGoogleMap";
    }

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public String CreateUser(EventForm eventForm) {
            eventDAO.createEvent(eventForm);
            //EventInfo.class.getDeclaredFields()[1].getAnnotations()[0];
        return "userInfoPage";
    }

}
