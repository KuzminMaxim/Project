package com.example.validation;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.UserModel;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserModel.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModel registrationForm = (UserModel) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.registrationForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.registrationForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.registrationForm.password");

        if (!this.emailValidator.isValid(registrationForm.getEmail())) {
            errors.rejectValue("email", "Pattern.registrationForm.email");
        } else if (registrationForm.getEmail() != null) {
            UserModel userDAOEmail = api.readSomethingOne(UserModel.class, registrationForm.getEmail(), "user_email");

            if (userDAOEmail != null) {
                errors.rejectValue("email", "Duplicate.registrationForm.email");
            }
        }

        if (!errors.hasFieldErrors("name")) {

            if (fieldIsValid(registrationForm.getName())){

                UserModel dbUser = api.readSomethingOne(UserModel.class, registrationForm.getName(), "user_name");

                if (dbUser != null) {
                    errors.rejectValue("name", "Duplicate.registrationForm.name");
                }

            } else errors.rejectValue("name", "Pattern.registrationForm.name");
            if (fieldLengthIsValid(registrationForm.getName())){
                errors.rejectValue("name","Pattern.registrationForm.length");
            }
        }
        if (registrationForm.getDecryptedPassword() != null){

            if (!fieldIsValid(registrationForm.getDecryptedPassword())){
                errors.rejectValue("password", "Pattern.registrationForm.password");
            }

            if (fieldLengthIsValid(registrationForm.getDecryptedPassword())){
                errors.rejectValue("password","Pattern.registrationForm.length");
            }

            if (!registrationForm.getDecryptedPassword().equals(registrationForm.getConfirmPassword())){
                errors.rejectValue("password","Pattern.registrationForm.confirm");
                errors.rejectValue("confirmPassword","Pattern.registrationForm.confirm");
            }

        }
    }

    private boolean fieldIsValid(String name){
        return name.matches("[а-яА-Яa-zA-Z0-9]+");
    }

    private boolean fieldLengthIsValid(String field){
        return field.length() <= 6;
    }

}