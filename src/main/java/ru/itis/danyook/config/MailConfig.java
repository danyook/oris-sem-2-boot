package ru.itis.danyook.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailConfig {
    private String content;
    private String subject;
    private String from;
    private String sender;
    private String to;
}