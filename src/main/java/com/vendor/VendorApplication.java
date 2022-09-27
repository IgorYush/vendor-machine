package com.vendor;

import com.vendor.models.ActiveUserStore;
import com.vendor.models.LogoutTokensStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VendorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendorApplication.class, args);
	}

	@Bean
	public ActiveUserStore activeUserStore(){
		return new ActiveUserStore();
	}

	@Bean
	public LogoutTokensStore logoutTokensStore(){
		return new LogoutTokensStore();
	}
}
