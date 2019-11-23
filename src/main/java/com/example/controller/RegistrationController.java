package com.example.controller;

import com.example.api.MyApi;
import com.example.dao.NewUserDAO;
import com.example.form.RegistrationForm;
import com.example.model.UserInfo;
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
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
public class RegistrationController {

    @Autowired
    private NewUserDAO registerDAO;

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
    public String showAccounts(Model model, Principal principal) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        List<UserInfo> list = registerDAO.getAccountsName();
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
    public String CreateUser(Model model,
                             @ModelAttribute("registrationForm") @Validated RegistrationForm registrationForm,
                             BindingResult result, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException {

        if (result.hasErrors()) {
            List<UserInfo> username = registerDAO.getAccountsName();
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
            List<UserInfo> username = registerDAO.getAccountsName();
            model.addAttribute("userInfo", username);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "registrationPage";
        }

        return "redirect:/welcome";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorP(Model model) {
        return "error";
    }


}


