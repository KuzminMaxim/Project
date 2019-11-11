package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.dao.NewUserDAO;
//import com.example.dao.RegisterDAO;
//import com.example.dao.AppRoleDAO;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NewUserDAO newUserDAO;

   // @Autowired
   // private AppRoleDAO appRoleDAO;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        UserInfo appUser = this.newUserDAO.findUserByName(name);

        if (appUser == null) {
            System.out.println("User not found! " + name);
            throw new UsernameNotFoundException("User " + name + " was not found in the database");
        }

        System.out.println("Found User: " + appUser);

        // [ROLE_USER, ROLE_ADMIN,..]
        List<String> roleNames = this.newUserDAO.getUserRole(appUser.getName());

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }

        return (UserDetails) new User(appUser.getName(), //
                appUser.getPassword(), grantList);
    }

}