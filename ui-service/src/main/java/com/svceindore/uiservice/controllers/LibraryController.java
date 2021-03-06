package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.model.BookDetail;
import com.svceindore.uiservice.model.BookHistory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Vijay Patidar
 * Date: 02/02/21
 * Time: 8:18 PM
 **/
@RequestMapping("/library")
@Controller()
public class LibraryController {

    private final KeycloakRestTemplate keycloakRestTemplate;

    public LibraryController(KeycloakRestTemplate keycloakRestTemplate) {
        this.keycloakRestTemplate = keycloakRestTemplate;
    }

    @RequestMapping("/library-map")
    public String getLibraryMap(Model model) {
        return "svgi-library-map";
    }

    @RequestMapping({"/add-book-detail.html"})
    public String addBookDetail() {
        return "add-book-detail";
    }

    @RequestMapping({"/issue-book.html"})
    public String issueBook() {
        return "issue-book";
    }

    @RequestMapping({"/submit-book.html"})
    public String submitBook() {
        return "submit-book";
    }

    @RequestMapping({"/explore-library-book.html"})
    public String exploreLibraryBook(HttpServletRequest request,@RequestParam(required = false,defaultValue = "") String query) {
        System.out.println(query);
        BookDetail[] books = keycloakRestTemplate.getForObject(
                "lb://library-service/api/library/bookDetail?query="+query.toLowerCase(),
                BookDetail[].class
        );
        request.setAttribute("books",books);
        request.setAttribute("query",query);
        return "explore-library-book";
    }

    @RequestMapping({"/add-book-copy.html"})
    public String addBookCopy(Model model, @RequestParam String bookId) {
        BookDetail detail = keycloakRestTemplate.getForObject(
                "lb://library-service/api/library/bookDetail/" + bookId,
                BookDetail.class
        );
        if (detail != null) {
            model.addAttribute("title", detail.getTitle());
        }
        model.addAttribute("bid", bookId);
        return "add-book-copy";
    }

    @RequestMapping({"/my-library-history.html"})
    public String selfTransaction(Model model,@RequestParam String type) {
        BookHistory[] histories = keycloakRestTemplate.getForObject(
                "lb://library-service/api/library/history/self/get_" + type,
                BookHistory[].class
        );
        model.addAttribute("type",type);
        model.addAttribute("histories", histories);
        return "my-library-transaction-book";
    }

    @RequestMapping({"/library-history.html"})
    public String allTransaction(Model model,@RequestParam String type) {
        BookHistory[] histories = keycloakRestTemplate.getForObject(
                "lb://library-service/api/library/history/get_" + type,
                BookHistory[].class
        );
        model.addAttribute("type",type);
        model.addAttribute("histories", histories);
        return "library-transaction-book";
    }

}
