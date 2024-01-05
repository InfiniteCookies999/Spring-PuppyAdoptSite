package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.RegistrationBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.EmailNotFoundException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.EmailTakenException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.WebUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class WebUserService implements UserDetailsService {

    private final WebUserRepository repository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;

    public WebUserService(WebUserRepository repository,
                          EmailVerificationService emailVerificationService,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<WebUser> getUser(UserDetails details) {
        return repository.findUserByEmailIgnoreCase(details.getUsername());
    }

    /**
     * Takes a registration DTO converts it to an entity and saves the user
     * to the database. Will additionally send an email containing a verification
     * token to the user's email address.
     *
     * @param body a registration body for a user.
     * @throws EmailTakenException if the email already exists in the database for users.
     */
    @Transactional
    public void registerUser(RegistrationBody body) throws EmailTakenException {
        if (repository.findUserByEmailIgnoreCase(body.getEmail()).isPresent()) {
            throw new EmailTakenException();
        }

        String encodedPassword = passwordEncoder.encode(body.getPassword());
        WebUser user = new WebUser(body.getEmail(), encodedPassword, body.getFirstName(), body.getLastName());
        repository.save(user);
        emailVerificationService.generateVerificationForUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        WebUser user = repository.findUserByEmailIgnoreCase(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
        return User.withUsername(email)
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .disabled(!user.isVerified()) // The account is disabled until the email has been verified
                .build();
    }
}
