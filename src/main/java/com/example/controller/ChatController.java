package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

    @RequestMapping("/chatChat")
    public String index(HttpServletRequest request, Model model) {
        String username = (String) request.getSession().getAttribute("username");

        if (username == null || username.isEmpty()) {
            return "redirect:/loginChat";
        }
        model.addAttribute("username", username);

        return "chatChat";
    }

    @RequestMapping(path = "/loginChat", method = RequestMethod.GET)
    public String showLoginPage() {
        return "loginChat";
    }

    @RequestMapping(path = "/loginChat", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username) {
        username = username.trim();

        if (username.isEmpty()) {
            return "loginChat";
        }
        request.getSession().setAttribute("username", username);

        return "redirect:/chatChat";
    }

    @RequestMapping(path = "/logoutChat")
    public String logout(HttpServletRequest request) {
        request.getSession(true).invalidate();

        return "redirect:/loginChat";
    }


}
