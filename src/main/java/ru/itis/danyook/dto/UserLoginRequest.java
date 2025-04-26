package ru.itis.danyook.dto;

public record UserLoginRequest(
        String username,
        String password
) {
}
