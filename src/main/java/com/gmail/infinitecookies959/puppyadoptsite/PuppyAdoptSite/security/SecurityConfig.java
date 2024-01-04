package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.security;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final WebUserService webUserService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(WebUserService webUserService, PasswordEncoder passwordEncoder) {
        this.webUserService = webUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates the security chain for HTTP requests enabling authorization
     * depending on the roles the user has.
     * */
    @Bean
    public SecurityFilterChain supplyHttpFilterChain(HttpSecurity http) throws Exception {
        return http
                // This can prevent cross site forgery but requires cookies. Disabling for now.
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/login",
                                        "/register",
                                        "/invalid-email-token",
                                        "/please-verify",
                                        "/verify",
                                        "/successful-verification",
                                        "/home").permitAll()
                                .anyRequest().hasRole("USER")
                )
                .formLogin(
                        form -> form
                                .loginProcessingUrl("/login")
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()
                )
                .build();
    }

    /**
     * Configure Spring security to use the appropriate authentication service
     * and password encoder.
     * */
    @Autowired
    public void configureAuthBuilder(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(webUserService)
                .passwordEncoder(passwordEncoder);
    }
}
