package com.example.form;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

//@MyEntity(name = "params")
public class RegistrationForm {

    //@MyId
    //@MyGeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;

    private String password;
    private MultipartFile[] avatar;
    private String email;
    private String decryptedPassword;

    public RegistrationForm() {}

    public RegistrationForm(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.decryptedPassword = password;
    }

    public RegistrationForm(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getRole() {
        return "ROLE_USER";
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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
