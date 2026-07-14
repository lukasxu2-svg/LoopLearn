package com.example.saastest.modules.benutzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class BenutzerController {

    @GetMapping("/{id}")
    public String getBenutzerById(@PathVariable Integer id) {
        return "Student with Id: " + id;
    }
}
