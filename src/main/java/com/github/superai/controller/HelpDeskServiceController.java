package com.github.superai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HelpDeskServiceController {

    @GetMapping({"", "/","/helpdesk"})
    public String viewHelpDesk(Model model) {
        return "viewhelpdesk";
    }

}
