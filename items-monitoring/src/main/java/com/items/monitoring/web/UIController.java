package com.items.monitoring.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class UIController {

    @GetMapping("/")
    public String home() {
        return "redirect:/search-ui";
    }

    @GetMapping("/search-ui")
    public String search() {
        return "search";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }
}
