package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;

@ObjectType(id = "users")
public class UserInfo {

    private int id;

    /*@Attribute(id = "user_avatar")*/
    private String url;

    @Attribute(id = "user_name")
    private String name;

    @Attribute(id = "user_password")
    private String password;

    @Attribute(id = "user_email")
    private String email;

    public UserInfo(){}

    public UserInfo(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public UserInfo(String email) {
    }

    public UserInfo(String url, String url1){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name + "/" + this.password;
    }

}
