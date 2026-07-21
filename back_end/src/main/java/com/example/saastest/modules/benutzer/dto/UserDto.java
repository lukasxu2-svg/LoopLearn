package com.example.saastest.modules.benutzer.dto;

public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName) {
}
