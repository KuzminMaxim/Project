package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.dao.EventDAO;
import com.example.model.EventModel;
import com.example.model.UserLogoutChatModel;
import com.example.model.UserModel;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping
@Controller
public class LoginController {

    @Autowired
    EventDAO eventDAO;

    @Autowired
    ApiForInteractingWithTheDatabase api;

    @GetMapping(value = { "/", "/welcome" })
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }


    @GetMapping(value = "/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping(value = "/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        System.out.println("User logged out");
        return "logoutSuccessfulPage";
    }

    @GetMapping(value = "/userInfo")
    public String userInfo(Model model, Principal principal) {

        String userName = principal.getName();
        System.out.println("Username: " + userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        String name = principal.getName();

        List<EventModel> eventsWhereCreator = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_name_of_creator");
        if (!eventsWhereCreator.isEmpty()){

            for (int i = 0; i < eventsWhereCreator.size(); i++){
                String id = eventsWhereCreator.get(i).getNameOfEvent() +
                        eventsWhereCreator.get(i).getEventDateOfCreation() + eventsWhereCreator.get(i).getNameOfEventCreator();
                List <UserLogoutChatModel> userLogoutChatModels = api.readAll(UserLogoutChatModel.class);
                String logoutTime = "";
                for (UserLogoutChatModel userLogoutChatModel : userLogoutChatModels) {

                    String chatId = eventsWhereCreator.get(i).getNameOfEvent() +
                            eventsWhereCreator.get(i).getEventDateOfCreation() + eventsWhereCreator.get(i).getNameOfEventCreator();

                    eventsWhereCreator.get(i).setId(chatId);

                    if (userLogoutChatModel.getUserLogoutTime().substring(24).equals(principal.getName())
                            && userLogoutChatModel.getChatId().equals(chatId)) {

                        logoutTime = userLogoutChatModel.getUserLogoutTime();

                        if (userLogoutChatModels.size() != 0){

                            if (i < userLogoutChatModels.size()){

                                    if (principal.getName().equals(eventsWhereCreator.get(i).getNameOfEventCreator()) &&
                                            eventsWhereCreator.get(i).getId().equals(userLogoutChatModel.getChatId())){
                                        EventModel countOfNewMessages = eventDAO.findCountOfNewMessagesAfterLogout(chatId, logoutTime);
                                        eventsWhereCreator.get(i).setCountOfNewMessages(countOfNewMessages.getCountOfNewMessages());
                                        break;
                                    }

                            }

                        }

                    }

                }
                EventModel countOfParticipants = eventDAO.findCountOfParticipants(id);
                eventsWhereCreator.get(i).setDate(eventsWhereCreator.get(i).getDate().replace("T", " "));
                eventsWhereCreator.get(i).setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("eventsWhereCreator", eventsWhereCreator);

        }

        List<EventModel> eventsWhereParticipant = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_participant");

        if (!eventsWhereParticipant.isEmpty()){

            for (int i = 0; i < eventsWhereParticipant.size(); i++){

                String id = eventsWhereParticipant.get(i).getNameOfEvent() +
                        eventsWhereParticipant.get(i).getEventDateOfCreation() + eventsWhereParticipant.get(i).getNameOfEventCreator();

                List <UserLogoutChatModel> userLogoutChatModels = api.readAll(UserLogoutChatModel.class);
                String logoutTime = "";

                for (UserLogoutChatModel userLogoutChatModel : userLogoutChatModels) {

                    String chatId = eventsWhereParticipant.get(i).getNameOfEvent() +
                            eventsWhereParticipant.get(i).getEventDateOfCreation() + eventsWhereParticipant.get(i).getNameOfEventCreator();

                    eventsWhereParticipant.get(i).setId(chatId);

                    if (userLogoutChatModel.getUserLogoutTime().substring(24).equals(principal.getName())
                            && userLogoutChatModel.getChatId().equals(chatId)) {

                        logoutTime = userLogoutChatModel.getUserLogoutTime();

                        if (userLogoutChatModels.size() != 0){

                            if (i < userLogoutChatModels.size()){

                                if (principal.getName().equals(eventsWhereParticipant.get(i).getEventParticipant()) &&
                                        eventsWhereParticipant.get(i).getId().equals(userLogoutChatModel.getChatId())){
                                    EventModel countOfNewMessages = eventDAO.findCountOfNewMessagesAfterLogout(chatId, logoutTime);
                                    eventsWhereParticipant.get(i).setCountOfNewMessages(countOfNewMessages.getCountOfNewMessages());
                                    break;
                                }

                            }

                        }

                    }

                }

                EventModel countOfParticipants = eventDAO.findCountOfParticipants(id);
                eventsWhereParticipant.get(i).setDate(eventsWhereParticipant.get(i).getDate().replace("T", " "));
                eventsWhereParticipant.get(i).setCountOfParticipant(countOfParticipants.getCountOfParticipant());

            }

            model.addAttribute("eventsWhereParticipant", eventsWhereParticipant);
        }

        List<EventModel> cancelledChats = eventDAO.findCancelledChats(name);
        if (!cancelledChats.isEmpty()){
            for (EventModel cancelledChat : cancelledChats) {
                EventModel countOfParticipants = eventDAO.findCountOfParticipants(cancelledChat.getId());
                cancelledChat.setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("cancelledChats", cancelledChats);
        }

        UserModel registrationForm = new UserModel();
        EventModel eventModel = new EventModel();
        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm", eventModel);



        return "userInfoPage";
    }

    @GetMapping(value = "/403")
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
