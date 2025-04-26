package ru.itis.danyook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.danyook.dto.ExchangeRateResponse;
import ru.itis.danyook.dto.UserLoginRequest;
import ru.itis.danyook.dto.UserRegistrationRequest;
import ru.itis.danyook.service.EmailService;
import ru.itis.danyook.service.ExchangeService;
import ru.itis.danyook.service.auth.AuthUserService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthUserService userService;
    private final EmailService emailService;

    private final Map<String, UserRegistrationRequest> pendingRegistrations = new ConcurrentHashMap<>();

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new UserRegistrationRequest("", "", "", "", ""));
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute UserRegistrationRequest registrationDto, Model model) {
        if (userService.findUserByEmail(registrationDto.email()) != null) {
            model.addAttribute("error", "Пользователь с данной почтой уже зарегистрирован.");
            return "redirect:/auth/registration";
        }

        String verificationCode = emailService.sendVerificationCode(registrationDto.email());
        pendingRegistrations.put(registrationDto.email(), registrationDto);

        model.addAttribute("email", registrationDto.email());
        return "verify";
    }

    @GetMapping("/registration/verify")
    public String showVerificationPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify";
    }

    @PostMapping("/registration/verify")
    public String verifyCode(@RequestParam String email,
                             @RequestParam String verificationCode,
                             Model model) {
        UserRegistrationRequest registrationDto = pendingRegistrations.get(email);
        if (registrationDto == null) {
            model.addAttribute("error", "Регистрация не была начата или ссылке истек срок.");
            return "verify";
        }

        if (emailService.verifyCode(email, verificationCode)) {
            userService.registerUser(registrationDto);
            pendingRegistrations.remove(email);

            model.addAttribute("message", "Регистрация прошла успешно!");
            return "success";
        } else {
            model.addAttribute("error", "Неверный код подтверждения. Попробуйте еще раз.");
            return "verify";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new UserLoginRequest("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserLoginRequest user, Model model) {
        if (userService.verify(user)) {
            return "success";
        }
        return "redirect:/auth/login";
    }
}
