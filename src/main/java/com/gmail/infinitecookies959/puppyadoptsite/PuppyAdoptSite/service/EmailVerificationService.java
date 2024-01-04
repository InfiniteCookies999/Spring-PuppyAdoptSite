package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.VerificationTokenNotFoundException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.EmailVerification;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.EmailVerificationRepository;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.repository.WebUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final WebUserRepository webUserRepository;
    private final JavaMailSender javaMailSender;

    @Value("${server.url}")
    private String serverUrl;

    public EmailVerificationService(EmailVerificationRepository emailVerificationRepository,
                                    WebUserRepository webUserRepository,
                                    JavaMailSender javaMailSender) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.webUserRepository = webUserRepository;
        this.javaMailSender = javaMailSender;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Sends a link containing the token for verification of a
     * user to the email.
     *
     * @param email email to send a verification link to.
     * @param token verification token.
     */
    private void sendVerificationEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@puppy-adopt.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Verification of email address");
        mailMessage.setText("Follow the link below to verify your email address:\n" +
                serverUrl + "/verify?token=" + token);

        javaMailSender.send(mailMessage);
    }

    /**
     * Will create a verification token for the user and send
     * a verification token to the user's email address.
     *
     * @param user user to send a verification token to.
     */
    @Transactional
    public void generateVerificationForUser(WebUser user) {
        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        String token = generateVerificationToken();
        verification.setToken(token);
        sendVerificationEmail(user.getEmail(), token);
        emailVerificationRepository.save(verification);
    }

    /**
     * Takes an existing verification token sent to the user
     * sets the user associated with the token as being verified.
     *
     * If the verification token does not exist for a user then
     * VerificationTokenNotFoundException is thrown.
     *
     * @param verificationToken a verification token sent to a user.
     * @throws VerificationTokenNotFoundException if the verification token is not associated with a user.
     */
    public void verifyUser(String verificationToken) throws VerificationTokenNotFoundException {
        EmailVerification emailVerification = emailVerificationRepository.findByToken(verificationToken)
                .orElseThrow(VerificationTokenNotFoundException::new);

        WebUser user = emailVerification.getUser();
        user.setVerified(true);
        webUserRepository.save(user);
    }
}
