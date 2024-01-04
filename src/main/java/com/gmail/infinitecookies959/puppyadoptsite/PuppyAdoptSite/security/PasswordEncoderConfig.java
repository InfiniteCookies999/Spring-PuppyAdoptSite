package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    /**
     * Supplies the password encoder to be used to encode passwords
     * for users.
     * */
    @Bean
    public PasswordEncoder supplyPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
