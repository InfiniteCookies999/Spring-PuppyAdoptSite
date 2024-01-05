package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.AdoptPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final AdoptPostService adoptPostService;

    public HomeController(AdoptPostService adoptPostService) {
        this.adoptPostService = adoptPostService;
    }

    @GetMapping(value = {"/", "/home"})
    public String home(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "breed", required = false, defaultValue = "any") String breedParam,
            @RequestParam(value = "age", required = false, defaultValue = "any") String ageParam,
            Model model,
            HttpServletRequest request
            ) {

        PuppyBreed breed = adoptPostService.parsePuppyBreed(breedParam);
        int lessThanAge = adoptPostService.parseLessThanAge(ageParam);

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", AdoptPostService.PAGE_SIZE);
        model.addAttribute("numPages", adoptPostService.getNumberOfPagePosts(breed, lessThanAge));
        List<AdoptPost> posts = adoptPostService.getPagePosts(pageNumber, Sort.Direction.DESC, breed, lessThanAge);
        model.addAttribute("posts", posts);
        String baseUrl = request.getRequestURI() + "?page=" + pageNumber;
        if (breed != null) {
            baseUrl += "&breed=" + breedParam;
        }
        if (lessThanAge != Integer.MAX_VALUE) {
            baseUrl += "&age=" + ageParam;
        }
        model.addAttribute("baseUrl", baseUrl);
        String[] breedValues = new String[PuppyBreed.values().length];
        int index = 0;
        for (PuppyBreed breedIdx : PuppyBreed.values()) {
            String breadValue = breedIdx.toString().toLowerCase().replaceAll("_", " ");
            breadValue = Character.toUpperCase(breadValue.charAt(0)) + breadValue.substring(1);
            breedValues[index++] = breadValue;
        }
        model.addAttribute("breadValues", breedValues);
        String selectedBreed = breedParam.replaceAll("-", " ")
                                         .replaceAll("_" , " ")
                                         .toLowerCase();
        model.addAttribute("selectedBreed", selectedBreed);

        return "home";
    }
}
