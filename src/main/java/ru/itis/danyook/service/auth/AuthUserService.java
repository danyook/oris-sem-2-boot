package ru.itis.danyook.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.danyook.dto.UserRegistrationRequest;
import ru.itis.danyook.model.UserEntity;
import ru.itis.danyook.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationRequest registrationDto) {
        UserEntity user = new UserEntity();
        user.setName(registrationDto.name());
        user.setUsername(registrationDto.username());
        user.setEmail(registrationDto.email());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));

        userRepository.save(user);
        log.info("User registered: {}", user.getEmail());
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
