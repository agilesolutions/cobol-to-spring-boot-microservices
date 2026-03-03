package com.agilesolutions.authorization.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

//@Component
public class JwtTokenCustomizer {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            // Only customize access tokens
            if (context.getTokenType().getValue().equals("access_token")) {

                // Add custom claims
                context.getClaims().claim("organization", "Acme Corp");

                // Add roles from client or user
                if (context.getPrincipal().getAuthorities() != null) {
                    context.getClaims().claim("roles",
                            context.getPrincipal().getAuthorities().stream()
                                    .map(a -> a.getAuthority())
                                    .toList()
                    );
                }

                // Example: add custom "department" claim
                context.getClaims().claim("department", "engineering");
            }
        };
    }
}