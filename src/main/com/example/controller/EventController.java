package com.example.controller;

import com.example.api.MyApi;
import com.example.form.ChatForm;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
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
        List<EventForm> list = myApi.readAll(EventForm.class);
        model.addAttribute("allEvents", list);
        return "eventPage";
    }

/////////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/createEvent", method = RequestMethod.GET)
    public String createEvent(Model model, Principal principal, @RequestParam(required = false) String abc) {
        System.out.println("123123123 " + abc);

        System.out.println(myApi.readSomethingOne(RegistrationForm.class, "admin@admin.com", "user_email").toString());

        EventForm form = new EventForm();
        ChatForm chatForm = new ChatForm();
        model.addAttribute("eventForm", form);
        model.addAttribute("chatForm", chatForm);

        List<EventForm> testList = myApi.readAll(EventForm.class);

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
            List<EventForm> eventName = myApi.readAll(EventForm.class);
            //List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            return "MyGoogleMap";
        }
        try {
            myApi.save(eventForm);
            myApi.save(chatForm);

        } catch (Exception e){
            List<EventForm> eventName = myApi.readAll(EventForm.class);
            //List<EventInfo> eventName = eventDAO.getEventsName();
            model.addAttribute("eventInfo", eventName);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "/createEvent";
        }
        return "redirect:/userInfo";
    }

}
