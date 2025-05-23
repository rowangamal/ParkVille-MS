package com.example.backend.config;

import com.example.backend.enums.Role;
import com.example.backend.service.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JWTFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/api/drivers/signup").permitAll()
                        .requestMatchers("/api/drivers/login").permitAll()
                        .requestMatchers("/api/admins/signup").permitAll()
                        .requestMatchers("/api/admins/login").permitAll()
                        .requestMatchers("/api/managers/signup").permitAll()
                        .requestMatchers("/api/managers/login").permitAll()
                                .requestMatchers("/lot/**").permitAll()
                                .requestMatchers("/dashboard/**").permitAll()
                                .requestMatchers("/reports/**").permitAll()
                                .requestMatchers("/api/report/**").permitAll()
                                .requestMatchers("/api/drivers/spot/reserve").permitAll()
                                .requestMatchers("/api/drivers/spot/arrive").permitAll()
                                .requestMatchers("/api/drivers/spot/leave").permitAll()
                                .requestMatchers("/api/all-lots").permitAll()
                                .requestMatchers("/api/drivers/spot/all").permitAll()
                                .requestMatchers("/api/drivers/spot/leave").permitAll()
                                .requestMatchers("/api/drivers/spot/arrive").permitAll()
                                .requestMatchers("/api/is-parking-lot-created").permitAll()
                                .requestMatchers("/api/drivers/spot/reserve").permitAll()
                        .requestMatchers("/api/managers/**").hasAuthority("ROLE_MANAGER")
                        .requestMatchers("/api/admins/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("api/drivers/**").hasAuthority("ROLE_DRIVER")
                        .requestMatchers("/ws/**").permitAll()
//                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    // talks to the auth
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Frontend origin
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies/auth headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
