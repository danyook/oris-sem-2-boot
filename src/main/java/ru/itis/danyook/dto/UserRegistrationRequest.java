package ru.itis.danyook.dto;


public record UserRegistrationRequest(
        String name,
        String username,
        String email,
        String password,
        String veryficationCode) {
}
