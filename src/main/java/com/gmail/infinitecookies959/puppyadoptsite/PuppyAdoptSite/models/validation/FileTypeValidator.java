package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    private final HashSet<String> acceptedTypes = new HashSet<>();

    @Override
    public void initialize(FileType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        acceptedTypes.addAll(Arrays.asList(constraintAnnotation.accepted()));
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return acceptedTypes.contains(value.getContentType());
    }
}
