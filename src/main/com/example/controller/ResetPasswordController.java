package com.example.controller;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ResetPasswordController {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    @Autowired
    private static Map<String, UserModel> currentTokenMap = new HashMap<>();

    @Autowired
    public JavaMailSender emailSender;

    @GetMapping(value = "/forgot")
    public String displayForgotPasswordPage(Model model) {
        UserModel form = new UserModel();
        model.addAttribute("form", form);
        return "forgotPassword";
    }

    @PostMapping(value = "/forgot")
    public String processForgotPasswordForm(Model model, UserModel userModel, HttpServletRequest request) {

        String userEmail = userModel.getEmail();

        System.out.println("EMAIL: " + userEmail);
        List<UserModel> userFromDBList = api.readAllWhereSomething(UserModel.class, userEmail, "user_email");
        UserModel userFromDB = new UserModel();

        for (UserModel value : userFromDBList) {
            userFromDB.setName(value.getName());
            userFromDB.setPassword(value.getPassword());
            userFromDB.setRole(value.getRole());
            userFromDB.setEmail(value.getEmail());
        }

        if (userFromDBList.isEmpty()){
            model.addAttribute("errorMessage", "We didn't find an account for that e-mail address.");
        } else {

            System.out.println("NAME: " + userFromDB.getName());

            userFromDB.setResetToken(UUID.randomUUID().toString());
            currentTokenMap.put(userFromDB.getResetToken(), userFromDB);

            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

            passwordResetEmail.setTo(userEmail);
            passwordResetEmail.setSubject("Reset password");

            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl
                    + "/reset?token=" + userFromDB.getResetToken());

            this.emailSender.send(passwordResetEmail);

            model.addAttribute("successMessage", "A password reset link has been sent to " + userEmail);

        }

        return "forgotPassword";

    }

   @GetMapping(value = "/reset")
    public String displayResetPasswordPage(@RequestParam("token") String token, Model model) {

        UserModel userFromToken = currentTokenMap.get(token);

        if (userFromToken != null){
            model.addAttribute("resetToken", token);
            UserModel form = new UserModel();
            model.addAttribute("form", form);
            model.addAttribute("username", userFromToken.getName());
        } else {
            model.addAttribute("errorMessage", "Oops!  This is an invalid password reset link.");
        }

        return "resetPasswordFromToken";
    }

    @PostMapping(value = "/reset")
    public String setNewPassword(UserModel userModel, Model model) {

        if (userModel.getPassword().isEmpty()){
            model.addAttribute("errorMessage", "Password field is empty." +
                    "Please, follow the steps to reset your password again.");
            return "resetPasswordFromToken";
        } else {
            UserModel userFromToken = currentTokenMap.get(userModel.getResetToken());

            if (userFromToken != null){
                currentTokenMap.remove(userModel.getResetToken());

                userModel.setId(userFromToken.getName() + userFromToken.getEmail());
                userModel.setEmail(userFromToken.getEmail());
                userModel.setRole(userFromToken.getRole());
                userModel.setName(userFromToken.getName());

                api.update(userModel);

                return "redirect:/login";
            } else {
                model.addAttribute("errorMessage", "Oops!  This is an invalid password reset link.");
                return "resetPasswordFromToken";
            }
        }
    }

}
