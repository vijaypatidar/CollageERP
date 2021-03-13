package com.svceindore.uiservice.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
@ControllerAdvice
public class MyErrorController implements ErrorController {

    Logger logger = Logger.getLogger(getClass().getSimpleName());

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "access-denied";
            }
        }
        model.addAttribute("message", "");
        return "505";
    }

    @ExceptionHandler({IllegalStateException.class})
    public ModelAndView instanceNotFound(IllegalStateException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("505");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

