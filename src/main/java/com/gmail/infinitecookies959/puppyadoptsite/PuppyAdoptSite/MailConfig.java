package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender supplyMailSender() {
        return new JavaMailSenderImpl();
    }
}
