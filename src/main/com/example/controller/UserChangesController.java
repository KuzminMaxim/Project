package com.example.controller;

import java.security.Principal;

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
import org.springframework.web.bind.annotation.RequestMethod;


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
    public String changePassword(UserModel registrationForm) {

        if (registrationForm.getPassword().isEmpty()){
            System.out.println("Password for user: '"+ registrationForm.getName() +"' was not changed.");
            return "/error";
        } else{
            api.update(registrationForm);
            System.out.println("Password for user: '"+ registrationForm.getName() +"' was changed.");
        }
        return "redirect:/userInfo";
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
