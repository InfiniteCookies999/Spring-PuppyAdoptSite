package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailNotFoundException extends UsernameNotFoundException {
    public EmailNotFoundException(String msg) {
        super(msg);
    }
}
