package com.agilesolutions.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableDiscoveryClient
public class AccountUpdateServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountUpdateServiceApplication.class, args);
    }
}