package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.EmailVerification;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
public class EmailVerificationRepositoryTests {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    private EmailVerification emailVerification;
    private WebUser user;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");

        emailVerification = new EmailVerification();
        emailVerification.setUser(user);
        emailVerification.setToken("The token");
    }

    @Test
    public void saveEmailVerificationReturnsEmailVerification() {

        webUserRepository.save(user);
        EmailVerification returnedEmailVerification = emailVerificationRepository.save(emailVerification);

        assertThat(returnedEmailVerification).isNotNull();
        assertThat(returnedEmailVerification.getId()).isGreaterThan(0);

    }

    @Test
    public void findEmailVerificationByTokenReturnsEmailVerification() {

        webUserRepository.save(user);
        emailVerificationRepository.save(emailVerification);

        Optional<EmailVerification> returnedEmailVerification =
                emailVerificationRepository.findByToken(emailVerification.getToken());

        assertThat(returnedEmailVerification).isNotNull();
        assertThat(returnedEmailVerification).isNotEmpty();

    }
}
