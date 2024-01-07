package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.validation.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class AdoptPostBody {

    @NotBlank
    @Length(min = 1, max = 40)
    private String puppyName;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer puppyAge;

    @NotBlank
    @Length(min = 10, max = 400)
    private String description;

    @NotNull
    private PuppyBreed breed;

    @NotNull
    @FileType(accepted = { "image/png", "image/jpeg", "image/jpg", "image/webp" })
    private MultipartFile puppyImage;

}
