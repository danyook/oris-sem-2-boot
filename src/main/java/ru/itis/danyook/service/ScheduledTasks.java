package ru.itis.danyook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itis.danyook.model.UserEntity;
import ru.itis.danyook.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final UserRepository userRepository;


    // Задача выполняется каждый день в 8:00 утра
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendCurrencyNotifications() {
        try {
            String exchangeRates = exchangeService.getExchangeRates();

            List<String> userEmails = userRepository.findAll().stream().map(UserEntity::getEmail).toList();

            for (String email : userEmails) {
                emailService.sendExchangeRate(email, exchangeRates);
            }

        } catch (Exception e) {
            log.error("Ошибка при отправке курс валют на почту: {}", e.getMessage());
        }
    }
}