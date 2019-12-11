package com.example.controller;

import com.example.api.MyApi;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping
@Controller
public class DeleteController {

    @Autowired
    private MyApi api;

    @RequestMapping(value = "/deleteSomething", method = RequestMethod.GET)
    public String viewDeletePage(Model model) {
        RegistrationForm registrationForm = new RegistrationForm();
        EventForm eventForm = new EventForm();
        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm",eventForm);
        return "deletePage";
    }

    @RequestMapping(value = "/deleteSomething", method = RequestMethod.POST)
    public String deleteUser(RegistrationForm registrationForm, EventForm eventForm)
            throws NoSuchFieldException, IllegalAccessException {

        if (registrationForm.getName() != null){
            api.delete(registrationForm);
        }

        if (eventForm.getNameOfEvent() != null){
            api.delete(eventForm);
            api.update(eventForm);
        }

        return "redirect:/userInfo";
    }

    @RequestMapping(value = "/deleteParticipantFromEvent", method = RequestMethod.POST)
    public String deleteParticipantFromEvent(EventForm eventForm)
            throws NoSuchFieldException, IllegalAccessException {

        if (eventForm.getNameOfEvent() != null){
            api.deleteOne(eventForm);
        }

        return "redirect:/userInfo";
    }

}
