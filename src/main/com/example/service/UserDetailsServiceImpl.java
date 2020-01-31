package com.example.service;

import com.example.api.ApiForInteractingWithTheDatabase;
import com.example.controller.ChatController;
import com.example.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ApiForInteractingWithTheDatabase api;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        if (fieldIsValid(name)){
            List<UserModel> appUser = api.readAllWhereSomething(UserModel.class, name, "user_name");

            if (appUser.size() == 0){
                logger.info("User {} not found!", name);
                throw new UsernameNotFoundException("User " + name + " was not found in the database");
            }

            String roleName = appUser.get(0).getRole();

            List<GrantedAuthority> grantList = new ArrayList<>();

            if (roleName != null){
                GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
                grantList.add(authority);
            }

            return new User(appUser.get(0).getName(),
                    appUser.get(0).getPassword(), grantList);
        }
        logger.info("Incorrect name: {}", name);
        throw new UsernameNotFoundException("User " + name + " was not found in the database");
    }

    private boolean fieldIsValid(String name){
        return name.matches("[а-яА-Яa-zA-Z0-9]+");
    }

}