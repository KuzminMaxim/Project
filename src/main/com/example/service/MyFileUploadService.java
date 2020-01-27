package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;

@Service
public class MyFileUploadService {

    @Autowired
    private UserDAO userDAO;

    public void preparingToGetFileUpload(Model model, Principal principal){

        UserModel myUploadForm = new UserModel();
        model.addAttribute("myUploadForm", myUploadForm);

        String avatarPath = userDAO.getAvatarPath(principal.getName());

        if (avatarPath != null){
            String avatarForTh = avatarPath.substring(40);
            model.addAttribute("avatarExists", avatarForTh);
        }

    }

    public void preparingToDoUpload(UserModel myUploadForm, Principal principal){

        String uploadRootPath = "C:\\Users\\Максим\\IdeaProjects\\MainProject\\uploads";

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

    }

}
