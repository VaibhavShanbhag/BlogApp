package com.blogapp.backend.Configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityFilterChainConfig {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityFilterChainConfig(AuthenticationEntryPoint authenticationEntryPoint, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(corsConifg -> corsConifg.configurationSource(corsConfigurationSource()));
        httpSecurity.csrf(csrfConfig -> csrfConfig.disable());
        httpSecurity.authorizeHttpRequests(
                requestMatcher->requestMatcher.requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/auth/refetch").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/posts/").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/posts/user/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/posts/post/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/comments/post/{postid}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/users/{username}").permitAll()
                        .anyRequest().authenticated()
        );

        httpSecurity.exceptionHandling(
                exceptionConfig->exceptionConfig.authenticationEntryPoint(authenticationEntryPoint)
        );

        httpSecurity.sessionManagement(
                sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Use allowedOriginPatterns with specific origins
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}
