package com.svceindore.uiservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Vijay Patidar
 * Date: 02/02/21
 * Time: 8:18 PM
 **/
@RequestMapping("/library")
@Controller()
public class LibraryController {

    @RequestMapping("/library-map")
    public String getLibraryMap(Model model){
        return "svgi-library-map";
    }

}
