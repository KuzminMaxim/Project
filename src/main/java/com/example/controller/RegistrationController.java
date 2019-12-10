package com.example.controller;

import com.example.api.MyApi;
import com.example.dao.NewUserDAO;
import com.example.form.RegistrationForm;
import com.example.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RequestMapping
@Controller
public class RegistrationController {

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private MyApi api;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == RegistrationForm.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @RequestMapping(value = "/viewRegisterUser", method = RequestMethod.GET)
    public String showAccounts(Model model) {
        RegistrationForm info = new RegistrationForm();
        List<RegistrationForm> list = api.readAll(info);
        model.addAttribute("listOfUser", list);
        return "registeredPage";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public String viewRegisterPage(Model model) {
        RegistrationForm form = new RegistrationForm();
        model.addAttribute("registrationForm", form);
        return "registrationPage";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String CreateUser(Model model,
                             @ModelAttribute("registrationForm") @Validated RegistrationForm registrationForm,
                             BindingResult result, HttpServletRequest request) {

        RegistrationForm newRegistrationForm = new RegistrationForm();

        if (result.hasErrors()) {
            List<RegistrationForm> username = api.readAll(newRegistrationForm);
            model.addAttribute("userInfo", username);
            return "registrationPage";
        }
        try {
            api.save(registrationForm);
            try {
                request.login(registrationForm.getName(),registrationForm.getDecryptedPassword());
                return "userInfoPage";
            } catch (ServletException e){
                System.out.println("Fail authentication");
            }
        } catch (Exception e) {
            List<RegistrationForm> username = api.readAll(newRegistrationForm);
            model.addAttribute("userInfo", username);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registrationPage";
        }

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorP() {
        return "error";
    }


}


