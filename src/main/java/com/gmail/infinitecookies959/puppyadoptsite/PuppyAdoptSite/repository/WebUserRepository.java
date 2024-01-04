package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    Optional<WebUser> findUserByEmailIgnoreCase(String email);

}
