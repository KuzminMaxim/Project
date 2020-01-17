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
import java.util.List;

@Service
public class LoginService {

    @Autowired
    EventDAO eventDAO;

    @Autowired
    ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    public void preparingToGetUserPage(Model model, Principal principal){

        String userName = principal.getName();
        logger.info("User {} logged in!", userName);
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


    }

}
