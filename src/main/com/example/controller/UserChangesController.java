package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.UserModel;
import com.example.utils.WebUtils;
import com.example.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RequestMapping
@Controller
public class UserChangesController {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    @Autowired
    private UserValidator userValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }

        if (target.getClass() == UserModel.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @GetMapping(value = "/changePassword")
    public String viewChangePasswordPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        UserModel form = new UserModel();

        model.addAttribute("registrationForm", form);

        return "changePasswordPage";
    }


    @PostMapping(value = "/changePassword")
    public String changePassword(@ModelAttribute("registrationForm") @Validated UserModel registrationForm,
                                 BindingResult result, Principal principal, Model model) {

        if (registrationForm.getPassword().isEmpty()){
            model.addAttribute("error", "New password is empty");
        } else {
            if (passwordIsValid(registrationForm.getDecryptedPassword())){
                try {
                    registrationForm.setName(principal.getName());
                    api.update(registrationForm);
                    model.addAttribute("success", "Password was changed");
                    model.addAttribute("registrationForm", registrationForm);
                    return "changePasswordPage";
                } catch (Exception e){
                    model.addAttribute("errorMessage", "Error: " + e.getMessage());
                    return "changePasswordPage";
                }
            } else {
                model.addAttribute("error", "New password is incorrect");
            }
        }
        model.addAttribute("registrationForm", registrationForm);
        return "changePasswordPage";
    }

    private boolean passwordIsValid(String password){
        return password.matches("[а-яА-Яa-zA-Z0-9]+") && password.length() > 6;
    }

    @GetMapping(value = "/setAvatar")
    public String viewSetAvatarPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        UserModel form = new UserModel();
        model.addAttribute("registrationForm", form);
        return "changeAvatarPage";
    }

}
