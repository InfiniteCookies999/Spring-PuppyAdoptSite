package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestAdoptPostFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.HSQLDB)
public class AdoptPostRepositoryTests {

    @Autowired
    private AdoptPostRepository adoptPostRepository;

    @Autowired
    private WebUserRepository webUserRepository;

    private WebUser user;

    private AdoptPost adoptPost1;
    private AdoptPost adoptPost2;
    private AdoptPost adoptPost3;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
    }

    @Test
    public void saveAdoptPostReturnsPost() {

        webUserRepository.save(user);
        AdoptPost returnedAdoptPost = adoptPostRepository.save(adoptPost1);

        assertThat(returnedAdoptPost).isNotNull();
        assertThat(returnedAdoptPost.getId()).isGreaterThan(0);

    }

    @Test
    public void findAllByPageReturnsPage() {

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);

        Pageable pageable1 = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        Pageable pageable2 = PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "id"));
        Pageable pageable3 = PageRequest.of(2, 2, Sort.by(Sort.Direction.DESC, "id"));
        List<AdoptPost> posts1 = adoptPostRepository.findAll(pageable1).toList();
        List<AdoptPost> posts2 = adoptPostRepository.findAll(pageable2).toList();
        List<AdoptPost> posts3 = adoptPostRepository.findAll(pageable3).toList();

        assertThat(posts1).isNotNull();
        assertThat(posts2).isNotNull();
        assertThat(posts3).isNotNull();
        assertThat(posts1.size()).isEqualTo(2);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts3.size()).isEqualTo(0);

    }

    @Test
    public void countByReturnsCount() {

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);

        long count = adoptPostRepository.count();

        assertThat(count).isEqualTo(3);

    }

    @Test
    public void findByBreedPageReturnsPage() {

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        AdoptPost adoptPost4 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);
        adoptPostRepository.save(adoptPost4);

        Pageable pageable1 = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        List<AdoptPost> posts1 = adoptPostRepository.findByBreed(PuppyBreed.BEAGLE, pageable1).toList();
        List<AdoptPost> posts2 = adoptPostRepository.findByBreed(PuppyBreed.BORDER_COLLIE, pageable1).toList();

        assertThat(posts1).isNotNull();
        assertThat(posts2).isNotNull();
        assertThat(posts1.size()).isEqualTo(1);
        assertThat(posts2.size()).isEqualTo(2);

    }

    @Test
    public void countByBreedReturnsCount() {

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        AdoptPost adoptPost4 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);
        adoptPostRepository.save(adoptPost4);

        long count1 = adoptPostRepository.countByBreed(PuppyBreed.BEAGLE);
        long count2 = adoptPostRepository.countByBreed(PuppyBreed.BORDER_COLLIE);

        assertThat(count1).isEqualTo(1);
        assertThat(count2).isEqualTo(3);

    }

    @Test
    public void findByPuppyAgeLessOrEqualReturnsPage() {

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);

        Pageable pageable1 = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id"));
        List<AdoptPost> posts1 = adoptPostRepository.findByPuppyAgeLessThanEqual(5, pageable1).toList();
        List<AdoptPost> posts2 = adoptPostRepository.findByPuppyAgeLessThanEqual(7, pageable1).toList();
        List<AdoptPost> posts3 = adoptPostRepository.findByPuppyAgeLessThanEqual(8, pageable1).toList();

        assertThat(posts1).isNotNull();
        assertThat(posts2).isNotNull();
        assertThat(posts3).isNotNull();
        assertThat(posts1.size()).isEqualTo(1);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts3.size()).isEqualTo(2);

    }

    @Test
    public void countByPuppyAgeLessOrEqualReturnsCount() {

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);

        long count1 = adoptPostRepository.countByPuppyAgeLessThanEqual(5);
        long count2 = adoptPostRepository.countByPuppyAgeLessThanEqual(8);

        assertThat(count1).isEqualTo(1);
        assertThat(count2).isEqualTo(3);

    }

    @Test
    public void findByBreedAndPuppyAgeLessOrEqualReturnsPage() {

        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);
        AdoptPost adoptPost4 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);
        adoptPostRepository.save(adoptPost4);

        Pageable pageable1 = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "id"));
        List<AdoptPost> posts1 = adoptPostRepository.findByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BEAGLE, 5, pageable1).toList();
        List<AdoptPost> posts2 = adoptPostRepository.findByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BORDER_COLLIE, 5, pageable1).toList();
        List<AdoptPost> posts3 = adoptPostRepository.findByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BORDER_COLLIE, 8, pageable1).toList();

        assertThat(posts1).isNotNull();
        assertThat(posts2).isNotNull();
        assertThat(posts3).isNotNull();
        assertThat(posts1.size()).isEqualTo(1);
        assertThat(posts2.size()).isEqualTo(1);
        assertThat(posts3.size()).isEqualTo(3);

    }

    @Test
    public void countByBreedAndPuppyAgeLessOrEqualReturnsCount() {

        adoptPost2 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 5);
        adoptPost1 = TestAdoptPostFactory.getPost(user, PuppyBreed.BEAGLE, 5);
        adoptPost3 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);
        AdoptPost adoptPost4 = TestAdoptPostFactory.getPost(user, PuppyBreed.BORDER_COLLIE, 8);

        webUserRepository.save(user);
        adoptPostRepository.save(adoptPost1);
        adoptPostRepository.save(adoptPost2);
        adoptPostRepository.save(adoptPost3);
        adoptPostRepository.save(adoptPost4);

        long count1 = adoptPostRepository.countByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BEAGLE, 5);
        long count2 = adoptPostRepository.countByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BORDER_COLLIE, 5);
        long count3 = adoptPostRepository.countByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BORDER_COLLIE, 8);

        assertThat(count1).isEqualTo(1);
        assertThat(count2).isEqualTo(1);
        assertThat(count3).isEqualTo(3);

    }

}
