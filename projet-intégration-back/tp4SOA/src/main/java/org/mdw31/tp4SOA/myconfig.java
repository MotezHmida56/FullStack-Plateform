package org.mdw31.tp4SOA;

import org.glassfish.jersey.server.ResourceConfig;

import org.mdw31.tp4SOA.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class myconfig implements WebMvcConfigurer {

    // Jersey services registration
    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(TacheRS.class);
        resourceConfig.register(ChatRS.class);
        resourceConfig.register(ProjetService.class);
        resourceConfig.register(UserService.class);
        return resourceConfig;
    }

    // Cors configuration using CorsConfigurationSource bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8081")); // Allowed React origin
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Applies to all APIs
        return source;
    }

    // Configure CORS and disable CSRF for REST APIs
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()  // Allow all requests (no authentication for this path)
                        .anyRequest().authenticated())  // Any other request requires authentication
                .csrf(csrf -> csrf.disable())  // Disable CSRF for REST APIs
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));  // Apply the CORS configuration

        return http.build();
    }

    // BCrypt PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
