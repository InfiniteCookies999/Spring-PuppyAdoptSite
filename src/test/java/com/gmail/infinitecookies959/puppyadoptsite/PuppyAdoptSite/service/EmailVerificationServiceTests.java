package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.VerificationTokenNotFoundException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.EmailVerification;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.EmailVerificationRepository;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.WebUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationServiceTests {

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @Mock
    private WebUserRepository webUserRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    private WebUser user;

    private EmailVerification verification;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");

        verification = new EmailVerification();
        verification.setToken("The token");
        verification.setUser(user);
    }

    @Test
    public void generateVerificationForUser() {

        when(emailVerificationRepository.save(any())).thenReturn(verification);
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailVerificationService.generateVerificationForUser(user);

        verify(emailVerificationRepository, times(1)).save(any());
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

    }

    @Test
    public void verifyUserSetsUserVerified() throws VerificationTokenNotFoundException {

        EmailVerification verification = new EmailVerification();
        verification.setToken("The token");
        verification.setUser(user);

        when(emailVerificationRepository.findByToken(verification.getToken())).thenReturn(Optional.of(verification));
        when(webUserRepository.save(any())).thenReturn(user);

        emailVerificationService.verifyUser(verification.getToken());

        assertThat(user.isVerified()).isTrue();

    }

}
