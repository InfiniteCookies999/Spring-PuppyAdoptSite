package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite;

import com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.models.entity.WebUser;

public class TestWebUserFactory {

    public static WebUser getUser(String name) {
        WebUser user = new WebUser();
        user.setEmail(name + "@gmail.com");
        user.setPassword("Super_secret_password11$");
        user.setFirstName(name);
        user.setLastName("LastName");
        return user;
    }
}
