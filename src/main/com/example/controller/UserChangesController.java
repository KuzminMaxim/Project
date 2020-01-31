package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.UserModel;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;


@RequestMapping
@Controller
public class UserChangesController {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

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
    public String changePassword(UserModel registrationForm, Principal principal, Model model) {

        if (registrationForm.getPassword().isEmpty()){
            model.addAttribute("error", "New password is empty");
            System.out.println("Password for user: '"+ principal.getName() +"' was not changed.");
        } else {
            if (passwordIsValid(registrationForm.getDecryptedPassword())){
                registrationForm.setName(principal.getName());
                api.update(registrationForm);
                System.out.println("Password for user: '"+ registrationForm.getName() +"' was changed.");
                model.addAttribute("success", "Password was changed");
                model.addAttribute("registrationForm", registrationForm);
                return "changePasswordPage";
            } else {
                System.out.println("New password is incorrect!");
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
