package com.svceindore.uiservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Vijay Patidar
 * Date: 02/02/21
 * Time: 8:18 PM
 **/
@RequestMapping("/library")
@Controller()
public class LibraryController {

    private final RestTemplate restTemplate;

    public LibraryController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/library-map")
    public String getLibraryMap(Model model) {
        return "svgi-library-map";
    }

    @RequestMapping({"/add-book-detail.html"})
    public String addBookDetail() {
        return "add-book-detail";
    }

    @RequestMapping({"/add-book-copy.html"})
    public String addBookCopy(Model model, @RequestParam String bookId) {
        model.addAttribute("bid",bookId);
        return "add-book-copy";
    }

}
