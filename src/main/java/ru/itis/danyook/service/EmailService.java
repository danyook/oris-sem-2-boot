package ru.itis.danyook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final ExchangeService exchangeService;

    public String sendVerificationCode(String email) {
        String code = generateRandomCode();
        verificationCodes.put(email, code);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Код подтверждения");
            message.setText("Ваш код подтверждения: %s".formatted(code));
            mailSender.send(message);
            log.info("Email sent to {}", email);
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", email, ex.getMessage());
        }

        return code;
    }

    public boolean verifyCode(String email, String verificationCode) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(verificationCode)) {
            verificationCodes.remove(email);
            return true;
        }
        return false;
    }

    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public void sendExchangeRate(String email, String exchangeRates) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Курсы валют");
            message.setText("Курс валют на сегодня: %s".formatted(exchangeRates));
            mailSender.send(message);
            log.info("Email sent to {}", email);
        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}", email, ex.getMessage());
        }
    }
}
