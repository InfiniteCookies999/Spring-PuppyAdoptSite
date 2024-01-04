package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.EmailTakenException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.RegistrationBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.WebUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebUserServiceTests {

    @Mock
    private WebUserRepository webUserRepository;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private WebUserService webUserService;

    private WebUser user;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");
    }

    @Test
    public void loadUserByUsernameReturnsUserDetails() {

        when(webUserRepository.findUserByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = webUserService.loadUserByUsername(user.getEmail());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    public void registerUserSavesUser() throws EmailTakenException {

        RegistrationBody registrationBody = new RegistrationBody(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName());

        when(webUserRepository.findUserByEmailIgnoreCase(user.getEmail())).thenReturn(Optional.empty());
        when(webUserRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        doNothing().when(emailVerificationService).generateVerificationForUser(any());

        webUserService.registerUser(registrationBody);

        verify(webUserRepository, times(1)).save(any());
        verify(emailVerificationService, times(1)).generateVerificationForUser(any());

    }

}
