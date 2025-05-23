package com.metodologia.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestRolesController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Hola admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String accessUser(){
        return "Hola User";
    }

    @GetMapping("/invitado")
    @PreAuthorize("hasRole('invited')")
    public String accessInvited(){
        return "Hola invitado";
    }
}
