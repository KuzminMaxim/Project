package com.example.controller;

import com.example.model.UserModel;
import com.example.service.MyFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping
@Controller
public class MyFileUploadController {

    @Autowired
    private MyFileUploadService myFileUploadService;

    @GetMapping(value = "/uploadOneFile")
    public String uploadOneFileHandler(Model model, Principal principal) {

        myFileUploadService.preparingToGetFileUpload(model, principal);

        return "uploadOneFile";
    }

    @PostMapping(value = "/uploadOneFile")
    public String uploadOneFileHandlerPOST(@ModelAttribute("myUploadForm") UserModel myUploadForm, Principal principal) {
        return this.doUpload(myUploadForm, principal);
    }

    private String doUpload(UserModel myUploadForm, Principal principal) {

        myFileUploadService.preparingToDoUpload(myUploadForm, principal);

        return "redirect:/userInfo";
    }
}