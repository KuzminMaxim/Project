package com.example.form;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class RegistrationForm {

    private long id;
    private String userName;
    private String password;
    private String avatar;

    public RegistrationForm() {}

    public RegistrationForm(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public RegistrationForm(long id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getEnable() {
        return 1;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return userName;
    }

    public void setName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.isEmpty()){
            this.password=password;
        }else{
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }

}
