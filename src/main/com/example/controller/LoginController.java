package com.example.controller;

import com.example.service.LoginService;
import com.example.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping
@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @GetMapping(value = { "/", "/welcome" })
    public String welcomePage(Model model) {
        model.addAttribute("message", "Welcome to this application!");
        return "welcomePage";
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }


    @GetMapping(value = "/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping(value = "/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        logger.info("User logged out!");
        return "logoutSuccessfulPage";
    }

    @GetMapping(value = "/userInfo")
    public String userInfo(Model model, Principal principal) {

        loginService.preparingToGetUserPage(model, principal);

        return "userInfoPage";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hello, " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

}
