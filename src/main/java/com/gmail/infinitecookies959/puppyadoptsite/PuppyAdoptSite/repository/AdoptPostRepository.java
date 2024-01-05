package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptPostRepository extends JpaRepository<AdoptPost, Long> {

    Page<AdoptPost> findByBreed(PuppyBreed breed, Pageable pageable);

    Page<AdoptPost> findByPuppyAgeLessThanEqual(Integer puppyAge, Pageable pageable);

    Page<AdoptPost> findByBreedAndPuppyAgeLessThanEqual(PuppyBreed breed, Integer puppyAge, Pageable pageable);

    long countByBreed(PuppyBreed breed);

    long countByPuppyAgeLessThanEqual(Integer puppyAge);

    long countByBreedAndPuppyAgeLessThanEqual(PuppyBreed breed, Integer puppyAge);

}
