package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.EmailTakenException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.VerificationTokenNotFoundException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.RegistrationBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.EmailVerificationService;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final WebUserService webUserService;
    private final EmailVerificationService emailVerificationService;

    public AuthController(WebUserService webUserService, EmailVerificationService emailVerificationService) {
        this.webUserService = webUserService;
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/invalid-email-token")
    public String invalidEmailToken() {
        return "invalid-email-token";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registrationBody", new RegistrationBody());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegistrationBody registrationBody,
                           BindingResult bindingResult,
                           Model model) {
        model.addAttribute("registrationBody", registrationBody);
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            webUserService.registerUser(registrationBody);
        } catch (EmailTakenException e) {
            bindingResult.rejectValue("email", "error.email", "Email Taken");
            return "register";
        }

        // Successfully registered the user. Transferring them to a page to tell them to please-verify.
        return "redirect:/please-verify";
    }

    @GetMapping("/please-verify")
    public String pleaseVerify() {
        return "please-verify";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token) {
        try {
            emailVerificationService.verifyUser(token);
        } catch (VerificationTokenNotFoundException e) {
            return "redirect:/invalid-email-token";
        }
        return "redirect:/successful-verification";
    }

    @GetMapping("/successful-verification")
    public String successfulVerification() {
        return "/successful-verification";
    }
}
