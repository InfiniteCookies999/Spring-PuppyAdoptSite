package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.AdoptPostBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.AdoptPostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdoptPostService {

    public static final int PAGE_SIZE = 8;

    private final AdoptPostRepository repository;

    public AdoptPostService(AdoptPostRepository repository) {
        this.repository = repository;
    }

    public Optional<AdoptPost> getAdoptPost(Long postId) {
        return repository.findById(postId);
    }

    /**
     * Converts a {@code AdoptPostBody} to a database entity and saves it
     * into the database.
     *
     * @param user an authenticated user that is creating a post.
     * @param body the contents of the post.
     * @throws IOException if there is an issue reading the bytes of the uploaded
     *                     puppy file.
     */
    public void createNewPost(WebUser user, AdoptPostBody body) throws IOException {
        AdoptPost adoptPost = new AdoptPost();
        adoptPost.setPuppyName(body.getPuppyName());
        adoptPost.setPuppyAge(body.getPuppyAge());
        adoptPost.setBreed(body.getBreed());
        adoptPost.setDescription(body.getDescription());
        adoptPost.setUser(user);

        MultipartFile puppyImage = body.getPuppyImage();
        adoptPost.setImageOfPuppy(puppyImage.getBytes());
        adoptPost.setImageOfPuppyName(puppyImage.getOriginalFilename());

        repository.save(adoptPost);
    }

    /**
     * Parses a {@code String} into a {@code PuppyBreed}
     * by converting to uppercase and replacing - with _ characters.
     *
     * If it fails to parse this method returns null.
     *
     * @param breed the {@code String} to parse into a {@code PuppyBreed} or null.
     * @return a parsed {@code PuppyBreed}.
     */
    public PuppyBreed parsePuppyBreed(String breed) {
        breed = breed.toUpperCase().replaceAll("-", "_");
        try {
            return PuppyBreed.valueOf(breed);
        } catch (IllegalArgumentException ignore) {
            return null;
        }
    }

    /**
     * Parses a {@code String} into an integer. If it fails
     * to parse this method returns @{code Integer.MAX_VALUE}.
     *
     * @param lessThanAge the {@code String} to parse into an integer or {@code Integer.MAX_VALUE}.
     * @return a parsed integer or {@code Integer.MAX_VALUE}.
     */
    public int parseLessThanAge(String lessThanAge) {
        try {
            return Integer.parseInt(lessThanAge);
        } catch (NumberFormatException ignore) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Gets a page of posts which is a list containing at most {@value #PAGE_SIZE} which
     * meet the constraints of {@code direction}, {@code breed}, and {@code lessThanAge}.
     *
     * @param pageNumber which page to select. This value should be greater than or equal to 1.
     * @param direction if the posts should be selected in ascending or descending order from the
     *                  database.
     * @param breed all posts should meet this breed type. If {@code null} is passed any breed is allowed.
     * @param lessThanAge all posts should have a puppy's age less than or equal to this age.
     * @return A list of adoption posts meeting the required constraints.
     */
    public List<AdoptPost> getPagePosts(int pageNumber, Sort.Direction direction, PuppyBreed breed, int lessThanAge) {
        Pageable pageable = PageRequest.of(pageNumber - 1, PAGE_SIZE, Sort.by(direction, "id"));

        if (breed != null && lessThanAge != Integer.MAX_VALUE) {
            return repository.findByBreedAndPuppyAgeLessThanEqual(breed, lessThanAge, pageable).toList();
        } else if (breed != null) {
            return repository.findByBreed(breed, pageable).toList();
        } else if (lessThanAge != Integer.MAX_VALUE) {
            return repository.findByPuppyAgeLessThanEqual(lessThanAge, pageable).toList();
        } else {
            return repository.findAll(pageable).toList();
        }
    }

    /**
     * Gets the number of pages of posts given the constraints.
     *
     * @param breed all posts should meet this breed type. If {@code null} is passed any breed is allowed.
     * @param lessThanAge all posts should have a puppy's age less than or equal to this age.
     * @return The number of pages of posts given the constraints.
     */
    public int getNumberOfPagePosts(PuppyBreed breed, int lessThanAge) {
        long count = 0;

        if (breed != null && lessThanAge != Integer.MAX_VALUE) {
            count = repository.countByBreedAndPuppyAgeLessThanEqual(breed, lessThanAge);
        } else if (breed != null) {
            count = repository.countByBreed(breed);
        } else if (lessThanAge != Integer.MAX_VALUE) {
            count = repository.countByPuppyAgeLessThanEqual(lessThanAge);
        } else {
            count = repository.count();
        }

        return (int) Math.ceil(count / (double) PAGE_SIZE);
    }
}
