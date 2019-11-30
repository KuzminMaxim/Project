package com.example.controller;

import com.example.api.MyApi;
import com.example.form.ChatForm;
import com.example.form.EventForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;

@Controller
public class JoinController {

    @Autowired
    private MyApi myApi;

    @RequestMapping(value = "/joinToEvent", method = RequestMethod.POST)
    public String CreateUser(EventForm eventForm, Principal principal, Model model, ChatForm chatForm) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (eventForm.getNameOfEventCreator().equals(principal.getName())){
            String errorOfJoin = "You is creator of this event!";
            model.addAttribute("errorOfJoin", errorOfJoin);
            return "MyGoogleMap";
        } else {
            myApi.add(eventForm);
            myApi.add(chatForm);
            //myApi.readOne("event");
        }
        return "MyGoogleMap";
    }

}
