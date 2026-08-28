package com.example.saastest.modules.benutzer.repository;

import com.example.saastest.modules.benutzer.entity.Benutzer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Benutzer> findByEmail(String email);
}
