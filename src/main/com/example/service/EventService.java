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
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    ApiForInteractingWithTheDatabase apiForInteractingWithTheDatabase;

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    public void preparingGetEvent(String id, Model model){

        EventModel form = new EventModel();
        ChatModel chatModel = new ChatModel();
        model.addAttribute("eventModel", form);
        model.addAttribute("chatModel", chatModel);

        List<EventModel> testList;

        if (id == null){
            testList = apiForInteractingWithTheDatabase.readAll(EventModel.class);
        } else {
            testList = apiForInteractingWithTheDatabase
                    .readAllWhereSomething(EventModel.class, id, "event_id");
        }

        String[] eventName = new String[testList.toArray().length];
        Double[] eventLat = new Double[testList.toArray().length];
        Double[] eventLng = new Double[testList.toArray().length];
        String[] eventDescription = new String[testList.toArray().length];
        String[] dates = new String[testList.toArray().length];
        String[] eventNameOfCreator = new String[testList.toArray().length];
        String[] dateOfCreation = new String[testList.toArray().length];
        String[] eventStatus = new String[testList.toArray().length];
        String[] eventId = new String[testList.toArray().length];

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
                eventId[n] = testList.get(i).getId();
            }
        }

        model.addAttribute("eventName", eventName);
        model.addAttribute("eventLat", eventLat);
        model.addAttribute("eventLng", eventLng);
        model.addAttribute("eventDescript", eventDescription);
        model.addAttribute("eventDate", dates);
        model.addAttribute("eventNameOfCreator", eventNameOfCreator);
        model.addAttribute("dateOfCreation", dateOfCreation);
        model.addAttribute("eventId", eventId);
    }

    public void preparingPostEvent(EventModel eventModel, ChatModel chatModel, Principal principal){

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String dateOfCreation = ts.toString();

        String nameOfCreator = principal.getName();

        eventModel.setNameOfEventCreator(nameOfCreator);
        chatModel.setChatNameOfCreator(nameOfCreator);

        saveEventInDB(eventModel, chatModel, dateOfCreation);
    }

    public void resultHasErrors(Model model){
        List<EventModel> eventName = apiForInteractingWithTheDatabase.readAll(EventModel.class);
        model.addAttribute("eventInfo", "Field contains invalid characters");
    }

    private void saveEventInDB(EventModel eventModel, ChatModel chatModel, String dateOfCreation){
        eventModel.setEventDateOfCreation(dateOfCreation);
        chatModel.setChatDateOfCreation(dateOfCreation);

        apiForInteractingWithTheDatabase.save(eventModel);
        apiForInteractingWithTheDatabase.save(chatModel);
        logger.info("Event {} and chat {} was created", eventModel.getNameOfEvent(), chatModel.getChatName());
    }

    public void preparingLinkToEvent(Model model, EventModel eventModel, HttpServletRequest request){

        List<EventModel> event = apiForInteractingWithTheDatabase
                .readAllWhereSomething(EventModel.class, eventModel.getId(), "event_id");

        for (EventModel link : event){
            model.addAttribute("nameOfEventToLink", link.getNameOfEvent());
        }

        String linkToEvent = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +
                "/createEvent?id=" + eventModel.getId();


        model.addAttribute("linkToEvent", linkToEvent);

    }

    public void preparingCompleteEvent(EventModel eventModel){

        eventModel.setEventStatus("complete");

        apiForInteractingWithTheDatabase.update(eventModel);
    }

}
