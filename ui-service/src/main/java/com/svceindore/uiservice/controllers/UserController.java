package com.svceindore.uiservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by Vijay Patidar
 * Date: 26/02/21
 * Time: 9:05 PM
 **/
@Controller
@RequestMapping("/user/")
public class UserController {

    @GetMapping("/my-profile.html")
    public String getMyProfilePage(Model model, Principal principal){
        model.addAttribute("username",principal.getName());

        return "user-profile-page";
    }

    @GetMapping("/reset-user-password.html")
    public String getResetPasswordPage(){
        return "reset-user-password";
    }
}
