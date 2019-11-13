package com.example.controller;

import com.example.dao.NewEventDAO;
import com.example.form.EventForm;
import com.example.model.EventInfo;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import java.security.Principal;
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

    @RequestMapping(value = "/createEvent", method = RequestMethod.GET)
    public String createEvent(Model model) {
        EventForm form = new EventForm();
        model.addAttribute("eventForm", form);
        return "MyGoogleMap";
    }

    @RequestMapping(value = "/createEvent", method = RequestMethod.POST)
    public String CreateUser(Model model,
                             EventForm eventForm, Principal principal) {
            eventDAO.createEvent(eventForm);
            //EventInfo.class.getDeclaredFields()[1].getAnnotations()[0];
        return "userInfoPage";
    }

}
