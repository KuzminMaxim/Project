package com.example.model;

import com.example.api.Attribute;
import com.example.api.ObjectType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

@ObjectType(id = "users")
public class UserModel {

    private String resetToken;

    private String decryptedPassword;

    private String confirmPassword;

    @Attribute(id = "user_name")
    private String name;

    @Attribute(id = "user_password")
    private String password;

    @Attribute(id = "user_avatar")
    private MultipartFile[] avatar;

    @Attribute(id = "user_id")
    private String id;

    @Attribute(id = "user_email")
    private String email;

    @Attribute(id = "user_role")
    private String role = "ROLE_USER";

    public UserModel() {}

    public UserModel(String name){
        this.name = name;
    }

    public UserModel(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.decryptedPassword = password;
    }

    public UserModel(long id, String name, String password) {
        //this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MultipartFile[] getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile[] avatar) {
        this.avatar = avatar;
    }

    public int getEnable() {
        return 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setPassword(String password) {
        if (password.isEmpty()){
            this.password=password;
        }else{
            this.decryptedPassword = password;
            this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
