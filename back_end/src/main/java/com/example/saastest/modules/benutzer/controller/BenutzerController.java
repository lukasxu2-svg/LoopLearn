package com.example.saastest.modules.benutzer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.saastest.modules.benutzer.service.BenutzerService;

@RestController
@RequestMapping("api/users")
public class BenutzerController {
    private final BenutzerService service;

    public BenutzerController(BenutzerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public void getBenutzerById(@PathVariable Long id) {
        service.getBenutzerById(id);
    }
}
