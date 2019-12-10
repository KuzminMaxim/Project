package com.example.controller;

import com.example.api.MyApi;
import com.example.dao.NewEventDAO;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@RequestMapping
@Controller
public class LoginController {

    @Autowired
    NewEventDAO eventDAO;

    @Autowired
    MyApi api;

    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        System.out.println("User logged out");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        String userName = principal.getName();
        System.out.println("Username: " + userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        String name = principal.getName();


        EventForm eventInfo = new EventForm();
        List<EventForm> eventsWhereCreator = api.readAllWhereSomething(eventInfo, principal.getName(), "event_name_of_creator");
        if (!eventsWhereCreator.isEmpty()){
            for (EventForm eventForm : eventsWhereCreator) {
                String x = eventForm.getNameOfEvent();
                EventForm countOfParticipants = eventDAO.findCountOfParticipants(x);
                eventForm.setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("eventsWhereCreator", eventsWhereCreator);
        }

        List<EventForm> eventsWhereParticipant = api.readAllWhereSomething(eventInfo, principal.getName(), "event_participant");
        if (!eventsWhereParticipant.isEmpty()){
            for (EventForm eventForm : eventsWhereParticipant) {
                String x = eventForm.getNameOfEvent();
                EventForm countOfParticipants = eventDAO.findCountOfParticipants(x);
                eventForm.setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("eventsWhereParticipant", eventsWhereParticipant);
        }

        List<EventForm> cancelledChats = eventDAO.findCancelledChats(name);
        if (!cancelledChats.isEmpty()){
            for (EventForm cancelledChat : cancelledChats) {
                String x = cancelledChat.getNameOfEvent();
                EventForm countOfParticipants = eventDAO.findCountOfParticipants(x);
                cancelledChat.setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("cancelledChats", cancelledChats);
        }

        RegistrationForm registrationForm = new RegistrationForm();
        EventForm eventForm = new EventForm();
        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm",eventForm);



        return "userInfoPage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hello, " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

}
