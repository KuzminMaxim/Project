package com.example.controller;

import com.example.dao.NewEventDAO;
import com.example.form.EventForm;
import com.example.form.RegistrationForm;
import com.example.model.EventInfo;
import com.example.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    NewEventDAO eventDAO;

    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        model.addAttribute("title", "Welcome");
        model.addAttribute("message", "This is welcome page!");
        return "welcomePage";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "adminPage";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        System.out.println("User logged out");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) throws Exception {

        String userName = principal.getName();
        System.out.println("Username: " + userName);
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        String name = principal.getName();
        List<EventInfo> eventsWhereCreator = eventDAO.findEventsWhereCreator(name);
        if (!eventsWhereCreator.isEmpty()){
            model.addAttribute("eventsWhereCreator", eventsWhereCreator);
        }
        List<EventInfo> eventsWhereParticipant = eventDAO.findEventsWhereParticipant(name);
        if (!eventsWhereParticipant.isEmpty()){
            model.addAttribute("eventsWhereParticipant", eventsWhereParticipant);
        }

        RegistrationForm registrationForm = new RegistrationForm();
        EventForm eventForm = new EventForm();
        model.addAttribute("registrationForm", registrationForm);
        model.addAttribute("eventForm",eventForm);

        return "userInfoPage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = WebUtils.toString(loginedUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hello, " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }

    /*    @RequestMapping(value = "/cover={avatarNAME}", method = RequestMethod.GET)
    public BufferedImage userAvatar(Model model, Principal principal, ServletContext servletContext,
                                    @PathVariable String avatarNAME) throws Exception {
        String userName = principal.getName();
        UserInfo path = userDAO.findImageByName(userName);
        String url = path.getUrl();
        System.out.println("url: " + url);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        BufferedImage image = ImageIO.read(new File(url));
        ImageIO.write(image,"image.png",bao);
        System.out.println(ImageIO.write(image,"image.png",bao));
/////////////////////////////////////////////////////////////////
        String rootPath = System.getProperty("catalina.home");
        System.out.println("reeeerefefdcsdsdf:;::::: "+rootPath);
        *//*Book book = bookService.getBookById(Long.parseLong(bookId));
        String format = book.getImageSource().split("\\.")[1];

        ByteArrayOutputStream out = null;
        InputStream input = null;
        try{
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(rootPath + File.separator + book.getImageSource()));
            int data = 0;
            while ((data = input.read()) != -1){
                out.write(data);
            }
        }
        finally{
            if (null != input){
                input.close();
            }
            if (null != out){
                out.close();
            }
        }
        byte[] bytes = out.toByteArray();

        final HttpHeaders headers = new HttpHeaders();
        if (format.equals("png"))
            headers.setContentType(MediaType.IMAGE_PNG);
        if (format.equals("jpg"))
            headers.setContentType(MediaType.IMAGE_JPEG);
        if (format.equals("gif"))
            headers.setContentType(MediaType.IMAGE_GIF);*//*
        return image;
    }*/

 /*   @RequestMapping(value = "/photo", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] testphoto(ServletContext servletContext) throws IOException {
        InputStream in = servletContext.getResourceAsStream("/images/no_image.jpg");
        //OutputStream outputStream = new ObjectOutputStream(OutputStream.nullOutputStream());
        //IOUtils.copy(in, outputStream);
        return ;
    }*/

}
