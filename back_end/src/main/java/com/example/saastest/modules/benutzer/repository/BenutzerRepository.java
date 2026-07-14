package com.example.saastest.modules.benutzer.repository;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenutzerRepository extends JpaRepository<Benutzer, Integer> {
    Benutzer findByName(String name);

}
