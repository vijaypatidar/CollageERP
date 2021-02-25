package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.model.exam.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 25/02/21
 * Time: 2:24 PM
 **/
@Controller
@RequestMapping("/exam/online")
public class OnlineExamController {

    @RequestMapping("/paper")
    public String getOnlinePaper(Model model) {
        List<Question> questions = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add("Language");
        options.add("Framework");
        options.add("Library");
        options.add("Software");

        for(int i=1;i<=10;i++){
            questions.add(new Question(i,
                    "What is spring boot.",
                    null,
                    options,
                    i%2==0?i%3==0?1:2:3
            ));
        }

        model.addAttribute("paperTitle", "MST-1");
        model.addAttribute("questions", questions);
        model.addAttribute("time","2 Hours");
        model.addAttribute("endTimeMillisecond","1614269432832");
        return "online-exam-question-paper";
    }
}
