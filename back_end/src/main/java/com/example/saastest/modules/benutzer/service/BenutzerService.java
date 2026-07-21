package com.example.saastest.modules.benutzer.service;

import org.springframework.stereotype.Service;

import com.example.saastest.modules.benutzer.repository.BenutzerRepository;

@Service
public class BenutzerService {
    private final BenutzerRepository repo;

    public BenutzerService(BenutzerRepository repository) {
        this.repo = repository;
    }

    public void getBenutzerById(Long id) {
        repo.findById(id);
    }

    public void createBenutzer() {

    }

    public void deleteBenutzerById(Integer id) {

    }
}
