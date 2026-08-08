package com.springbootinit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "user-management";
    }

    @GetMapping("/user")
    public String userPage() {
        return "user-management";
    }
}
