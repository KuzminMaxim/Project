package com.example.service;

import com.example.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendEmail(UserModel registrationForm){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(registrationForm.getEmail());
        message.setSubject("Greeting");
        message.setText("Welcome to the \"Events on the map\" application. We are glad that you are with us!");

        this.emailSender.send(message);
    }

}
