package com.example.controller;

import java.io.InputStream;
import java.security.Principal;

import com.example.dao.NewUserDAO;
import com.example.form.RegistrationForm;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Part;


@Controller
public class UserChangesController {

    @Autowired
    private NewUserDAO registerDAO;

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String viewChangePasswordPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        RegistrationForm form = new RegistrationForm();

        model.addAttribute("registrationForm", form);

        return "changePasswordPage";
    }


    @RequestMapping (value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(RegistrationForm registrationForm){

        if (registrationForm.getPassword().isEmpty()){
            System.out.println("Password for user: '"+ registrationForm.getName() +"' was not changed.");
            return "/error";
        } else{
            registerDAO.changePassword(registrationForm);
            System.out.println("Password for user: '"+ registrationForm.getName() +"' was changed.");
        }
        return "redirect:/userInfo";
    }

    @RequestMapping(value = "/setAvatar", method = RequestMethod.GET)
    public String viewSetAvatarPage(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);
        RegistrationForm form = new RegistrationForm();
        model.addAttribute("registrationForm", form);
        return "changeAvatarPage";
    }

/*
    @RequestMapping (value = "/setAvatar", method = RequestMethod.POST)
    public String setAvatar(RegistrationForm registrationForm){
            registerDAO.setAvatar(registrationForm);
            System.out.println("Avatar for user: '"+ registrationForm.getName() +"' was changed.");
        return "redirect:/userInfo";
    }*/


}
