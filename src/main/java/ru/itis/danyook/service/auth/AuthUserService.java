package ru.itis.danyook.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.danyook.dto.UserLoginRequest;
import ru.itis.danyook.dto.UserRegistrationRequest;
import ru.itis.danyook.model.UserEntity;
import ru.itis.danyook.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public void registerUser(UserRegistrationRequest registrationDto) {
        UserEntity user = new UserEntity();
        user.setName(registrationDto.name());
        user.setUsername(registrationDto.username());
        user.setEmail(registrationDto.email());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));

        userRepository.save(user);
        log.info("User registered: {}", user.getEmail());
    }

    public boolean verify(UserLoginRequest user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.username(),
                        user.password()));
        if (authentication.isAuthenticated()) {
            return true;
        }
        return false;
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
