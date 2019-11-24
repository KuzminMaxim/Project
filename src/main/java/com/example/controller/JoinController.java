package com.example.controller;

import com.example.api.MyApi;
import com.example.form.EventForm;
import com.example.model.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class JoinController {

    @Autowired
    private MyApi myApi;

    @RequestMapping(value = "/joinToEvent", method = RequestMethod.POST)
    public String CreateUser(EventForm eventForm) throws NoSuchFieldException, IllegalAccessException {
            myApi.add(eventForm);
        return "MyGoogleMap";
    }

}
