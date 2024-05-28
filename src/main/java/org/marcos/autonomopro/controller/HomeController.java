package org.marcos.autonomopro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
public class HomeController {

    @GetMapping("/index")
    public String index() {
        return "fragments/index";
    }

    @GetMapping({"/", "/auth/login"}) // Ambas rutas llevarán al login
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro") //l
    public String mostrarRegistro() {
        return "registro";
    }
} 