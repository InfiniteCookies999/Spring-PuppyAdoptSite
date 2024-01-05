package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.TestWebUserFactory;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions.EmailTakenException;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.EmailVerificationService;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebUserService webUserService;

    @MockBean
    private EmailVerificationService emailVerificationService;

    private WebUser user;

    @BeforeEach
    public void init() {
        user = TestWebUserFactory.getUser("maddie");
    }

    @Test
    public void getRegisterPageContainsRegistrationBodyModel() throws Exception {

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registrationBody"));

    }

    @Test
    public void postRegistrationBodyRedirectsToPleaseVerifyPage() throws Exception {

        doNothing().when(webUserService).registerUser(any());

        mockMvc.perform(post("/register")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName()))
                .andExpect(redirectedUrl("/please-verify"))
                .andExpect(status().isFound());

    }

    @Test
    public void postRegistrationBodyHasErrors() throws Exception {

        doNothing().when(webUserService).registerUser(any());

        mockMvc.perform(post("/register")
                        .param("email", "not a valid email")
                        .param("password", "notvalidpassword")
                        .param("firstName", "")
                        .param("lastName", ""))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registrationBody", "email"))
                .andExpect(model().attributeHasFieldErrors("registrationBody", "password"))
                .andExpect(model().attributeHasFieldErrors("registrationBody", "firstName"))
                .andExpect(model().attributeHasFieldErrors("registrationBody", "lastName"))
                .andExpect(status().isOk());

    }

    @Test
    public void postRegistrationBodyEmailTaken() throws Exception {

        doThrow(EmailTakenException.class).when(webUserService).registerUser(any());

        mockMvc.perform(post("/register")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("firstName", user.getFirstName())
                        .param("lastName", user.getLastName()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("registrationBody", "email"))
                .andExpect(status().isOk());

    }

    @Test
    public void verifyEmailTokenRedirectsSuccessVerification() throws Exception {

        doNothing().when(emailVerificationService).verifyUser(any());

        mockMvc.perform(get("/verify")
                    .param("token", "thetoken"))
                .andExpect(redirectedUrl("/successful-verification"))
                .andExpect(status().isFound());

    }
}
