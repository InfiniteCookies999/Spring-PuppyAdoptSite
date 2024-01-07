package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.PuppyBreed;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.AdoptPost;
import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;

public class TestAdoptPostFactory {

    public static AdoptPost getPost(WebUser user, PuppyBreed breed, Integer age) {
        AdoptPost adoptPost = new AdoptPost();
        adoptPost.setPuppyName("Paco");
        adoptPost.setUser(user);
        adoptPost.setDescription("Description");
        adoptPost.setPuppyAge(age);
        adoptPost.setImageOfPuppyName("image.png");
        adoptPost.setImageOfPuppy(new byte[]{});
        adoptPost.setBreed(breed);
        return adoptPost;
    }
}
