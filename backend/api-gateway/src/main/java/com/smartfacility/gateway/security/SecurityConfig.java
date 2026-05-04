package com.smartfacility.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

 @Bean
 public SecurityWebFilterChain springSecurityFilterChain(
 ServerHttpSecurity http) {

   return http
   .csrf(csrf -> csrf.disable())
   .authorizeExchange(ex -> ex

   .pathMatchers(HttpMethod.OPTIONS).permitAll()

   .pathMatchers("/auth/**").permitAll()

   .anyExchange().permitAll()
   )
   .build();
 }
}