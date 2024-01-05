package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.RegistrationBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.AdoptPostRepository;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class PopulateDatabase {

    private final WebUserService webUserService;
    private final AdoptPostRepository adoptPostRepository;

    public PopulateDatabase(WebUserService webUserService, AdoptPostRepository adoptPostRepository) {
        this.webUserService = webUserService;
        this.adoptPostRepository = adoptPostRepository;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            /*RegistrationBody body = new RegistrationBody();
            body.setEmail("infinitecookies959@gmail.com");
            body.setPassword("SecretPass@!1");
            body.setFirstName("Cookies");
            body.setLastName("LastName");

            webUserService.registerUser(body);

            WebUser user = webUserService.getUser(
                    webUserService.loadUserByUsername("infinitecookies959@gmail.com")).get();
            user.setVerified(true);

            Random random = new Random();
            PuppyBreed[] breeds = PuppyBreed.values();
            for (int i = 0; i < 200; i++) {
                AdoptPost post = new AdoptPost();
                post.setPuppyName("Pet Name " + i);
                post.setDescription("This is a description for the pet");
                post.setPuppyAge(4);
                post.setBreed(breeds[random.nextInt(breeds.length)]);
                post.setImageName("placeholder.png");
                post.setUser(user);
                adoptPostRepository.save(post);
            }*/
        };
    }
}
