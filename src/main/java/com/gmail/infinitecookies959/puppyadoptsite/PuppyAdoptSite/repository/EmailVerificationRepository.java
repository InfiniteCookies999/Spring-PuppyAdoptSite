package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByToken(String token);

}
