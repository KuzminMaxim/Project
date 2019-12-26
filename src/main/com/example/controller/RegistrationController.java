package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.UserModel;
import com.example.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RequestMapping
@Controller
public class RegistrationController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    @Autowired
    public JavaMailSender emailSender;

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

    @GetMapping(value = "/viewRegisterUser")
    public String showAccounts(Model model) {
        List<UserModel> list = api.readAll(UserModel.class);
        model.addAttribute("listOfUser", list);
        return "registeredPage";
    }

    @GetMapping(value = "/createUser")
    public String viewRegisterPage(Model model) {
        UserModel form = new UserModel();
        model.addAttribute("registrationForm", form);
        return "registrationPage";
    }

    @PostMapping(value = "/createUser")
    public String CreateUser(Model model,
                             @ModelAttribute("registrationForm") @Validated UserModel registrationForm,
                             BindingResult result, HttpServletRequest request) {

        String newIdForUser = registrationForm.getName() + registrationForm.getEmail();

        if (result.hasErrors()) {
            List<UserModel> username = api.readAll(UserModel.class);
            model.addAttribute("userInfo", username);
            return "registrationPage";
        }
        try {

            registrationForm.setId(newIdForUser);
            api.save(registrationForm);


            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(registrationForm.getEmail());
            message.setSubject("Greeting");
            message.setText("Welcome to the \"Events on the map\" application. We are glad that you are with us!");

            this.emailSender.send(message);


            try {

                request.login(registrationForm.getName(),registrationForm.getDecryptedPassword());
                return "userInfoPage";

            } catch (ServletException e){
                System.out.println("Fail authentication");
            }
        } catch (Exception e) {
            List<UserModel> username = api.readAll(UserModel.class);
            model.addAttribute("userInfo", username);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registrationPage";
        }

        return "redirect:/welcome";
    }

    @GetMapping(value = "/error")
    public String errorP() {
        return "error";
    }


}


