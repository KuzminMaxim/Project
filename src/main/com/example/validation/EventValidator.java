package com.example.validation;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.EventModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class EventValidator implements Validator {

    @Autowired
    ApiForInteractingWithTheDatabase api;

    @Override
    public boolean supports(Class<?> clazz) {return clazz == EventModel.class;}

    @Override
    public void validate(Object target, Errors errors) {

        EventModel eventModel = (EventModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nameOfEvent", "NotEmpty.eventForm.nameOfEvent");

        if (!fieldIsValid(eventModel.getNameOfEvent())){
            errors.rejectValue("nameOfEvent", "Pattern.eventModel.name");
        }
    }

    private boolean fieldIsValid(String field){
        return field.matches("[а-яА-Яa-zA-Z0-9]+");
    }
}
