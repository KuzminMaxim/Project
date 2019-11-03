package com.example.controller;

import java.security.Principal;
import java.util.List;

import com.example.dao.RegisterDAO;
import com.example.form.RegistrationForm;
import com.example.model.AppUser;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegistrationController {

    @Autowired
    private RegisterDAO registerDAO;

    @RequestMapping(value = "/viewRegisterUser", method = RequestMethod.GET)
    public String showAccounts(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        List<AppUser> list = registerDAO.getAccounts();
        model.addAttribute("appUser", list);
        return "registeredPage";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public String viewRegisterPage(Model model) {

        RegistrationForm form = new RegistrationForm();

        model.addAttribute("registrationForm", form);

        return "registrationPage";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String processCreateUser(RegistrationForm registrationForm) {
        if (registrationForm.getName().isEmpty() || registrationForm.getPassword().isEmpty()){
            System.out.println("User not created");
            return "/error";
        } else{
            registerDAO.insertUser(registrationForm);
            registerDAO.insertRole();
            System.out.println("Create user: " + registrationForm.getName());
        }
        return "redirect:/welcome";
    }
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorP(Model model) {
        return "errorPage";
    }


}


