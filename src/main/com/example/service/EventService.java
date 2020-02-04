package com.example.service;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.controller.EventController;
import com.example.model.ChatModel;
import com.example.model.EventModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    ApiForInteractingWithTheDatabase apiForInteractingWithTheDatabase;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    public void preparingGetEvent(String latitude, Model model){

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
        String[] eventStatus = new String[testList.toArray().length];
        int n = 0;
        for (int i = 0; i < testList.toArray().length; i++, n++){
            eventStatus[n] = testList.get(i).getEventStatus();
            if (eventStatus[n].equals("active")){
                eventName[n] = testList.get(i).getNameOfEvent();
                eventLat[n] = Double.valueOf(testList.get(i).getLatitude());
                eventLng[n] = Double.valueOf(testList.get(i).getLongitude());
                eventDescription[n] = testList.get(i).getDescriptionOfEvent();
                dates[n] = testList.get(i).getDate().replace("T", " ");
                eventNameOfCreator[n] = testList.get(i).getNameOfEventCreator();
                dateOfCreation[n] = testList.get(i).getEventDateOfCreation();
            }
        }

        model.addAttribute("eventName", eventName);
        model.addAttribute("eventLat", eventLat);
        model.addAttribute("eventLng", eventLng);
        model.addAttribute("eventDescript", eventDescription);
        model.addAttribute("eventDate", dates);
        model.addAttribute("eventNameOfCreator", eventNameOfCreator);
        model.addAttribute("dateOfCreation", dateOfCreation);

    }

    public void preparingPostEvent(EventModel eventModel, ChatModel chatModel){

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String dateOfCreation = ts.toString();

        String newIdForEventAndChat = eventModel.getNameOfEvent() + dateOfCreation + eventModel.getNameOfEventCreator();

        saveEventInDB(eventModel, chatModel, newIdForEventAndChat, dateOfCreation);
    }

    public void resultHasErrors(Model model){
        List<EventModel> eventName = apiForInteractingWithTheDatabase.readAll(EventModel.class);
        model.addAttribute("eventInfo", "Field contains invalid characters");
    }

    private void saveEventInDB(EventModel eventModel, ChatModel chatModel, String newIdForEventAndChat, String dateOfCreation){
        eventModel.setId(newIdForEventAndChat);
        eventModel.setEventDateOfCreation(dateOfCreation);
        chatModel.setId(newIdForEventAndChat);
        chatModel.setChatDateOfCreation(dateOfCreation);

        apiForInteractingWithTheDatabase.save(eventModel);
        apiForInteractingWithTheDatabase.save(chatModel);
        logger.info("Event {} and chat {} was created", eventModel.getNameOfEvent(), chatModel.getChatName());
    }

    public void preparingLinkToEvent(Model model, EventModel eventModel, HttpServletRequest request){

        String linkToEvent = "<a href=\"" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                "/createEvent?latitude=" + eventModel.getLatitude() +
                "&longitude=" + eventModel.getLongitude() + "\">" + eventModel.getNameOfEvent() + "</a>";

        model.addAttribute("nameOfEventToLink", eventModel.getNameOfEvent());
        model.addAttribute("linkToEvent", linkToEvent);

    }

    public void preparingCompleteEvent(EventModel eventModel){

        String newIdForEventAndChat = eventModel.getNameOfEvent() + eventModel.getEventDateOfCreation() + eventModel.getNameOfEventCreator();

        eventModel.setId(newIdForEventAndChat);
        eventModel.setEventStatus("complete");

        apiForInteractingWithTheDatabase.update(eventModel);
    }

}
