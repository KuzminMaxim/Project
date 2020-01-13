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

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
    public String uploadOneFileHandlerPOST(HttpServletRequest request, //
                                           Model model, //
                                           @ModelAttribute("myUploadForm") UserModel myUploadForm, Principal principal) {



        return this.doUpload(request, model, myUploadForm, principal, userDAO);

    }

    private String doUpload(HttpServletRequest request, Model model, //
                            UserModel myUploadForm, Principal principal, UserDAO userDAO) {

        String uploadRootPath = "C:\\Users\\Максим\\IdeaProjects\\MainProject\\resources\\static\\uploads\\avatars";
        System.out.println("uploadRootPath=" + uploadRootPath);

        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        MultipartFile[] fileDatas = myUploadForm.getAvatar();
        //
        List<File> uploadedFiles = new ArrayList<File>();
        List<String> failedFiles = new ArrayList<String>();

        for (MultipartFile fileData : fileDatas) {

            // Client File Name
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0) {
                try {
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator +
                            principal.getName() + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();

                    uploadedFiles.add(serverFile);
                    System.out.println("Write file: " + serverFile);

                    if (userDAO.getAvatarPath(principal.getName()) == null){
                        userDAO.addAvatar(serverFile.getAbsolutePath(), principal.getName());
                    } else {
                        userDAO.setAvatar(serverFile.getAbsolutePath(), principal.getName());
                    }

                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                    failedFiles.add(name);
                }

            }
        }

        model.addAttribute("uploadedFiles", uploadedFiles);
        model.addAttribute("failedFiles", failedFiles);
        return "uploadResult";
    }
}