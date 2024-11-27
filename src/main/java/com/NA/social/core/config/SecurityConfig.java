package com.NA.social.core.config;

import com.NA.social.core.filter.JwtFilter;
import com.NA.social.core.ultis.AccessDeinedHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.GET, "/api/files/**").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                            .requestMatchers("/api/public/**", "/ws/**",
                                    "/swagger-ui/**",
                                    "swagger-ui.html",
                                    "/v2/api-docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/webjars/**"
                            ).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(Customizer.withDefaults())
                .exceptionHandling((ex) -> ex.authenticationEntryPoint((req, res, authEx) -> {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, authEx.getMessage());
                }))
                .exceptionHandling((e) -> {
                    e.accessDeniedHandler(new AccessDeinedHandler());
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
