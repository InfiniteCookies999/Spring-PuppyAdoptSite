package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationBody {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        @Length(min = 5, max = 50)
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,50}$")
        private String password;
        @NotBlank
        @Length(min = 1, max = 40)
        private String firstName;
        @NotBlank
        @Length(min = 1, max = 40)
        private String lastName;
}
