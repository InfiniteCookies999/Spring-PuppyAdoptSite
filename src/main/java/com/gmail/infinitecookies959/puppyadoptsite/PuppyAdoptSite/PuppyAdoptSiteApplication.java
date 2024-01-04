package com.gmail.infinitecookies959.puppyadoptsite.PuppyAdoptSite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class PuppyAdoptSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(PuppyAdoptSiteApplication.class, args);
	}

	@Bean
	public JavaMailSender supplyMailSender() {
		return new JavaMailSenderImpl();
	}

}
