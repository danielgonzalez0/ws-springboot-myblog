package org.wildcodeschool.MyBlog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/articles/**").permitAll()// Permettre l'accès public aux endpoints GET sous /articles/
                        .requestMatchers(HttpMethod.POST, "/articles/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN") // Les endpoints POST sous /articles/ nécessitent un rôle USER ou ADMIN
                        .requestMatchers(HttpMethod.PUT, "/articles/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/articles/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/images/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/authors/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/authors/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/authors/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/authors/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers("/auth/**").permitAll() // Permettre l'accès public aux endpoints sous /auth/
                        .anyRequest().authenticated() // Tous les autres endpoints nécessitent une authentification
                )
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
