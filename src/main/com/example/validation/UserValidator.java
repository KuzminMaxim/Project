package com.example.validation;

import com.example.api.MyApi;
import org.apache.commons.validator.routines.EmailValidator;
import com.example.form.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private MyApi api;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == RegistrationForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationForm registrationForm = (RegistrationForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.registrationForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.registrationForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.registrationForm.password");

        if (!this.emailValidator.isValid(registrationForm.getEmail())) {
            errors.rejectValue("email", "Pattern.registrationForm.email");
        } else if (registrationForm.getEmail() != null) {

            RegistrationForm userDAOEmail = api.readSomethingOne(RegistrationForm.class, registrationForm.getEmail(), "user_email");

            if (userDAOEmail != null) {
                errors.rejectValue("email", "Duplicate.registrationForm.email");
            }
        }

        if (!errors.hasFieldErrors("name")) {

            RegistrationForm dbUser = api.readSomethingOne(RegistrationForm.class, registrationForm.getName(), "user_name");

            if (dbUser != null) {
                errors.rejectValue("name", "Duplicate.registrationForm.name");
            }
        }
    }

}