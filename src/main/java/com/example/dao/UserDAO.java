package com.example.dao;

import com.example.form.RegistrationForm;
import com.example.model.UserInfo;

import java.util.List;

public interface UserDAO {
    public List<UserInfo> getAccountsName();
    public UserInfo findUserByName(String name);
    public UserInfo findName(String name);
    public UserInfo findEmail(String email);
    public List<String> getUserRole(String name);
    public void insertUser(RegistrationForm registrationForm);
    public void changePassword(RegistrationForm registrationForm);
    public void setAvatar(RegistrationForm registrationForm);
}
