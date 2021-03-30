package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.clients.UserClient;
import com.svceindore.uiservice.model.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.QueryParam;
import java.security.Principal;

/**
 * Created by Vijay Patidar
 * Date: 26/02/21
 * Time: 9:05 PM
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/view-profile.html")
    public String getMyProfilePage(Model model, @RequestParam(required = false,defaultValue = "") String username, Principal principal){
        String u = username.isEmpty()?principal.getName():username;
        User user;
        if (username.isEmpty()){
            user = userClient.getCurrentUser();
        }else {
            user = userClient.getUser(username);
        }
        model.addAttribute("username",u);
        model.addAttribute("user",user);
        return "user-profile";
    }

    @GetMapping("/edit-profile-picture-page.html")
    public String editProfilePicturePage(Model model, @RequestParam(required = false,defaultValue = "") String username, Principal principal){
        String u = username.isEmpty()?principal.getName():username;
        model.addAttribute("username",u);
        return "edit-profile-picture-page";
    }



    @GetMapping("/reset-user-password.html")
    public String getResetPasswordPage(){
        return "reset-user-password";
    }
}
