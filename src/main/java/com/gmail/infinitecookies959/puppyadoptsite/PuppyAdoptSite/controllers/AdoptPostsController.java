package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.controllers;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.dto.AdoptPostBody;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.AdoptPostService;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.service.WebUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class AdoptPostsController {

    private static final String[] BREED_VALUES = new String[PuppyBreed.values().length];

    static {
        int index = 0;
        for (PuppyBreed breedIdx : PuppyBreed.values()) {
            String breadValue = breedIdx.toString().toLowerCase().replaceAll("_", " ");
            breadValue = Character.toUpperCase(breadValue.charAt(0)) + breadValue.substring(1);
            BREED_VALUES[index++] = breadValue;
        }
    }

    private final AdoptPostService adoptPostService;
    private final WebUserService webUserService;

    public AdoptPostsController(AdoptPostService adoptPostService, WebUserService webUserService) {
        this.adoptPostService = adoptPostService;
        this.webUserService = webUserService;
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
        model.addAttribute("breadValues", BREED_VALUES);
        String selectedBreed = breedParam.replaceAll("-", " ")
                .replaceAll("_" , " ")
                .toLowerCase();
        model.addAttribute("selectedBreed", selectedBreed);

        return "home";
    }

    @GetMapping("/images/puppy/{postId}")
    public ResponseEntity<byte[]> getPuppyImage(@PathVariable Long postId) {
        AdoptPost adoptPost = adoptPostService.getAdoptPost(postId).orElseThrow();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + adoptPost.getImageOfPuppyName() + "\"")
                .body(adoptPost.getImageOfPuppy());
    }

    @GetMapping("/new-post")
    public String getNewPostPage(Model model) {
        model.addAttribute("postBody", new AdoptPostBody());
        model.addAttribute("breadValues", BREED_VALUES);
        return "new-post";
    }

    @PostMapping("/new-post")
    public String publishPost(@ModelAttribute("postBody") @Valid AdoptPostBody adoptPostBody,
                              BindingResult bindingResult,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postBody", adoptPostBody);
            model.addAttribute("breadValues", BREED_VALUES);
            return "new-post";
        }

        WebUser user = webUserService.getUser(userDetails).orElseThrow();
        adoptPostService.createNewPost(user, adoptPostBody);

        return "redirect:/successful-new-post";
    }

    @GetMapping("/successful-new-post")
    public String successfulNewPost() {
        return "successful-new-post";
    }
}
