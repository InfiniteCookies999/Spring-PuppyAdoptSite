package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
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
public class WebUserRepositoryTests {

    @Autowired
    private WebUserRepository webUserRepository;

    private WebUser user;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");
    }

    @Test
    public void saveUserReturnsUser() {

        WebUser returnedUser = webUserRepository.save(user);

        assertThat(returnedUser).isNotNull();
        assertThat(returnedUser.getId()).isGreaterThan(0);
        assertThat(returnedUser.getEmail()).isEqualTo(user.getEmail());

    }

    @Test
    public void findUserByEmailReturnsUser() {

        webUserRepository.save(user);

        Optional<WebUser> optReturnedUser1 = webUserRepository.findUserByEmailIgnoreCase("maddie@gmail.com");
        Optional<WebUser> optReturnedUser2 = webUserRepository.findUserByEmailIgnoreCase("mAdDIe@gmaiL.coM");

        assertThat(optReturnedUser1).isNotNull();
        assertThat(optReturnedUser1).isNotEmpty();
        assertThat(optReturnedUser2).isNotNull();
        assertThat(optReturnedUser2).isNotEmpty();

    }
}
