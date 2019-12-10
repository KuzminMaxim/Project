package com.example.controller;

import com.example.api.MyApi;
import com.example.form.ChatForm;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
import com.example.model.EventInfo;
import com.example.validation.EventValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@RequestMapping
@Controller
public class EventController {

    /*@Autowired
    private NewEventDAO eventDAO;*/

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
        EventForm info = new EventForm();
        List<EventForm> list = myApi.readAll(info);
        model.addAttribute("allEvents", list);
        return "eventPage";
    }

/////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/createEvent", method = RequestMethod.GET)
    public String createEvent(Model model, Principal principal, @RequestParam(required = false) String abc) {
        System.out.println("123123123 " + abc);

        RegistrationForm registrationForm = new RegistrationForm();
        System.out.println(myApi.readSomethingOne(registrationForm, "admin@admin.com", "user_email").toString());

        EventForm form = new EventForm();
        ChatForm chatForm = new ChatForm();
        model.addAttribute("eventForm", form);
        model.addAttribute("chatForm", chatForm);

        List<EventForm> eventInfoList = myApi.readAllWhereSomething(form, principal.getName(), "event_name_of_creator");
        System.out.println("NEW LIST!!!");
        for (EventForm info : eventInfoList) {
            System.out.println("Creator: "+info.getNameOfEventCreator());
            System.out.println("name of event: " + info.getNameOfEvent());
            System.out.println("Date: " + info.getEventDateOfCreation());
            System.out.println();
        }

        List<ChatForm> chatFormList = myApi.readAllWhereSomething(chatForm, principal.getName(), "chat_participant");
        System.out.println();
        for (ChatForm info : chatFormList){
            System.out.println("Name: " + info.getChatName());
            System.out.println("Creator: " + info.getChatNameOfCreator());
            System.out.println("Participant: " + info.getChatParticipant());
            System.out.println("Status" + info.getChatStatus());
            System.out.println();
        }


        List<EventForm> testList = myApi.readAll(form);

        String[] eventName = new String[testList.toArray().length];
        Double[] eventLat = new Double[testList.toArray().length];
        Double[] eventLng = new Double[testList.toArray().length];
        String[] eventDescription = new String[testList.toArray().length];
        String[] dates = new String[testList.toArray().length];
        String[] eventNameOfCreator = new String[testList.toArray().length];
        int n = 0;
        for (int i = 0; i < testList.toArray().length; i++, n++){
            eventName[n] = testList.get(i).getNameOfEvent();
            eventLat[n] = Double.valueOf(testList.get(i).getLatitude());
            eventLng[n] = Double.valueOf(testList.get(i).getLongitude());
            eventDescription[n] = testList.get(i).getDescriptionOfEvent();
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
    public String createNewEvent(Model model, @ModelAttribute("eventForm") @Validated EventForm eventForm,
                             BindingResult result, ChatForm chatForm) throws SQLException {

        if (result.hasErrors()) {
            EventForm eventInfo = new EventForm();
            List<EventForm> eventName = myApi.readAll(eventInfo);
            //List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            return "MyGoogleMap";
        }
        try {
            myApi.save(eventForm);
            myApi.save(chatForm);

        } catch (Exception e){
            EventForm eventInfo = new EventForm();
            List<EventForm> eventName = myApi.readAll(eventInfo);
            //List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/createEvent";
        }
        return "redirect:/userInfo";
    }

}
