package com.example.validation;

import com.example.dao.NewEventDAO;
import com.example.form.EventForm;
import com.example.model.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {

    @Autowired
    private NewEventDAO newEventDAO;

    @Override
    public boolean supports(Class<?> clazz) {return clazz == EventForm.class;}

    @Override
    public void validate(Object target, Errors errors) {

        EventForm eventForm = (EventForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nameOfEvent", "NotEmpty.eventForm.nameOfEvent");

        if (!errors.hasFieldErrors("nameOfEvent")) {
            EventInfo dbUser = newEventDAO.findName(eventForm.getNameOfEvent());
            if (dbUser != null) {
                errors.rejectValue("nameOfEvent", "Duplicate.eventForm.nameOfEvent");
            }
        }

    }
}
