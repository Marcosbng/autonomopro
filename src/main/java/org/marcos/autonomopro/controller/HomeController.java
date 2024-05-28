package org.marcos.autonomopro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "fragments/index";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
} 