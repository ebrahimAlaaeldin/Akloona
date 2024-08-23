package com.example.akloona.Authentication;

import com.example.akloona.Profile.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutService logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authenticate/**") // list of request that should be permitted
                        .permitAll()
                        .anyRequest() // any other request should be authenticated
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // to make the session stateless
                )
                .authenticationProvider(authenticationProvider) // to add the authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // to add the filter before the UsernamePasswordAuthenticationFilter
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // to set the logout url
                        .addLogoutHandler(logoutHandler) // to add the logout handler
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // to set the logout success handler
                );

        return http.build();
    }
}