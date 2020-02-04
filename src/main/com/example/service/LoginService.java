package com.example.service;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.dao.EventDAO;
import com.example.model.EventModel;
import com.example.model.UserLogoutChatModel;
import com.example.model.UserModel;
import com.example.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    EventDAO eventDAO;

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public void preparingToGetUserPage(Model model, Principal principal){

        List<EventModel> allEventsWhereCreator = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_name_of_creator");
        List<EventModel> allEventsWhereParticipant = api.readAllWhereSomething(EventModel.class, principal.getName(), "event_participant");

        String userName = principal.getName();
        logger.info("User {} logged in!", userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        String name = principal.getName();

        List<EventModel> eventsWhereCreator = new ArrayList<>(allEventsWhereCreator);
        if (!eventsWhereCreator.isEmpty()){

            for (int i = 0; i < eventsWhereCreator.size(); i++){
                String status = eventsWhereCreator.get(i).getEventStatus();
                if (!status.equals("active")){
                    eventsWhereCreator.remove(i);
                    i--;
                }
            }

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

        List<EventModel> eventsWhereParticipant = new ArrayList<>(allEventsWhereParticipant);

        if (!eventsWhereParticipant.isEmpty()){

            for (int i = 0; i < eventsWhereParticipant.size(); i++){
                String status = eventsWhereParticipant.get(i).getEventStatus();
                if (!status.equals("active")){
                    eventsWhereParticipant.remove(i);
                    i--;
                }
            }

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

        List<EventModel> completedChats = eventDAO.findCompletedChats(name);
        if (!completedChats.isEmpty()){
            for (EventModel completedChat : completedChats) {
                EventModel countOfParticipants = eventDAO.findCountOfParticipants(completedChat.getId());
                completedChat.setCountOfParticipant(countOfParticipants.getCountOfParticipant());
            }
            model.addAttribute("completedEvents", completedChats);
        }


        UserModel registrationForm = new UserModel();
        EventModel eventModel = new EventModel();
        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm", eventModel);


    }

}
