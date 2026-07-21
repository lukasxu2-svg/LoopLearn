package com.example.saastest.modules.benutzer.repository;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {
    Optional<Benutzer> findByEmail(String email);
}
