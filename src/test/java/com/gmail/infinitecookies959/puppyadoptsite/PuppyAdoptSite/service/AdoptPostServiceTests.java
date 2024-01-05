package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.AdoptPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdoptPostServiceTests {

    @Mock
    private AdoptPostRepository adoptPostRepository;

    @InjectMocks
    private AdoptPostService adoptPostService;

    @Test
    public void parsePuppyBreedReturnsParsedBreed() {

        PuppyBreed breed1 = adoptPostService.parsePuppyBreed("any");
        PuppyBreed breed2 = adoptPostService.parsePuppyBreed("BORDER_COLLIE");
        PuppyBreed breed3 = adoptPostService.parsePuppyBreed("BORDER-COLLIE");
        PuppyBreed breed4 = adoptPostService.parsePuppyBreed("border_collie");
        PuppyBreed breed5 = adoptPostService.parsePuppyBreed("border-collie");

        assertThat(breed1).isNull();
        assertThat(breed2).isEqualTo(PuppyBreed.BORDER_COLLIE);
        assertThat(breed3).isEqualTo(PuppyBreed.BORDER_COLLIE);
        assertThat(breed4).isEqualTo(PuppyBreed.BORDER_COLLIE);
        assertThat(breed5).isEqualTo(PuppyBreed.BORDER_COLLIE);

    }

    @Test
    public void parseLessThanAgeReturnsAge() {

        int lessThanAge1 = adoptPostService.parseLessThanAge("any");
        int lessThanAge2 = adoptPostService.parseLessThanAge("5");

        assertThat(lessThanAge1).isEqualTo(Integer.MAX_VALUE);
        assertThat(lessThanAge2).isEqualTo(5);

    }

    @Test
    public void getNumberOfPagePostsByAnyBreedAnyAgeReturnsCount() {

        when(adoptPostRepository.count()).thenReturn(AdoptPostService.PAGE_SIZE*2L);

        int count = adoptPostService.getNumberOfPagePosts(null, Integer.MAX_VALUE);

        assertThat(count).isEqualTo(2);

    }

    @Test
    public void getNumberOfPagePostsBySpecificBreedAndAnyAgeReturnsCount() {

        when(adoptPostRepository.countByBreed(PuppyBreed.BORDER_COLLIE)).thenReturn(AdoptPostService.PAGE_SIZE*2L);

        int count = adoptPostService.getNumberOfPagePosts(PuppyBreed.BORDER_COLLIE, Integer.MAX_VALUE);

        assertThat(count).isEqualTo(2);

    }

    @Test
    public void getNumberOfPagePostsByAnyBreedAndSpecificAgeReturnsCount() {

        when(adoptPostRepository.countByPuppyAgeLessThanEqual(5)).thenReturn(AdoptPostService.PAGE_SIZE*2L);

        int count = adoptPostService.getNumberOfPagePosts(null, 5);

        assertThat(count).isEqualTo(2);

    }

    @Test
    public void getNumberOfPagePostsBySpecificBreedAndSpecificAgeReturnsCount() {

        when(adoptPostRepository.countByBreedAndPuppyAgeLessThanEqual(PuppyBreed.BORDER_COLLIE, 8)).thenReturn(AdoptPostService.PAGE_SIZE*2L);

        int count = adoptPostService.getNumberOfPagePosts(PuppyBreed.BORDER_COLLIE, 8);

        assertThat(count).isEqualTo(2);

    }

    @Test
    public void getPagePostsByAnyBreedAnyAgeReturnsPosts() {

        when(adoptPostRepository.findAll(any(Pageable.class))).thenReturn(Mockito.mock(Page.class));

        List<AdoptPost> posts = adoptPostService.getPagePosts(1, Sort.Direction.DESC, null, Integer.MAX_VALUE);

        assertThat(posts).isNotNull();

    }

    @Test
    public void getPagePostsBySpecificBreedAndAnyAgeReturnsPosts() {

        when(adoptPostRepository.findByBreed(any(), any(Pageable.class))).thenReturn(Mockito.mock(Page.class));

        List<AdoptPost> posts = adoptPostService.getPagePosts(1, Sort.Direction.DESC, PuppyBreed.POODLE, Integer.MAX_VALUE);

        assertThat(posts).isNotNull();

    }

    @Test
    public void getPagePostsByAnyBreedAndSpecificAgeReturnsPosts() {

        when(adoptPostRepository.findByPuppyAgeLessThanEqual(any(), any(Pageable.class))).thenReturn(Mockito.mock(Page.class));

        List<AdoptPost> posts = adoptPostService.getPagePosts(1, Sort.Direction.DESC, null, 5);

        assertThat(posts).isNotNull();

    }

    @Test
    public void getPagePostsBySpecificBreedAndSpecificAgeReturnsPosts() {

        when(adoptPostRepository.findByBreedAndPuppyAgeLessThanEqual(any(), any(), any(Pageable.class))).thenReturn(Mockito.mock(Page.class));

        List<AdoptPost> posts = adoptPostService.getPagePosts(1, Sort.Direction.DESC, PuppyBreed.POODLE, 5);

        assertThat(posts).isNotNull();

    }

}
