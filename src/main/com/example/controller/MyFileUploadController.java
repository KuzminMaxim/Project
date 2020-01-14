package com.example.controller;

import com.example.dao.UserDAO;
import com.example.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;

@RequestMapping
@Controller
public class MyFileUploadController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/uploadOneFile")
    public String uploadOneFileHandler(Model model, Principal principal) {

        UserModel myUploadForm = new UserModel();
        model.addAttribute("myUploadForm", myUploadForm);

        String avatarPath = userDAO.getAvatarPath(principal.getName());

        if (avatarPath != null){
            String avatarForTh = avatarPath.substring(65);
            model.addAttribute("avatarExists", avatarForTh);
        }
        return "uploadOneFile";
    }

    @PostMapping(value = "/uploadOneFile")
    public String uploadOneFileHandlerPOST(@ModelAttribute("myUploadForm") UserModel myUploadForm, Principal principal) {
        return this.doUpload(myUploadForm, principal, userDAO);
    }

    private String doUpload(UserModel myUploadForm, Principal principal, UserDAO userDAO) {

        String uploadRootPath = "C:\\Users\\Максим\\IdeaProjects\\MainProject\\resources\\uploads\\static\\avatars";

        File uploadRootDir = new File(uploadRootPath);

        MultipartFile[] fileDatas = myUploadForm.getAvatar();

        for (MultipartFile fileData : fileDatas) {

            String name = fileData.getOriginalFilename();

            if (name != null && name.length() > 0) {
                try {
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator +
                            principal.getName() + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();

                    if (userDAO.getAvatarPath(principal.getName()) == null){
                        userDAO.addAvatar(serverFile.getAbsolutePath(), principal.getName());
                    } else {
                        userDAO.setAvatar(serverFile.getAbsolutePath(), principal.getName());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return "redirect:/userInfo";
    }
}