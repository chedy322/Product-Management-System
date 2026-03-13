package com.example.demo.Infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


// products all private only for the user that has matching id 
// users only for admin but get/users/id only for user that has that id
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
        .csrf(csrf-> csrf.disable())
        .exceptionHandling(exception-> exception
            .accessDeniedHandler(accessDeniedHandler())
        )
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth-> auth.
            requestMatchers("/api/products/**").permitAll()
            .requestMatchers("/api/users/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        );
        // .authenticationProvider(authenticationProvider);

        
         // ── Add JWT filter BEFORE UsernamePassword filter ─
            // .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

     @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                {"error": "Access Denied", "message": "You don't have permission to access this resource"}
                """);
        };
    }

}
