package com.ward.ward_server.global.config;

import com.ward.ward_server.api.user.auth.security.CustomAccessDeniedHandler;
import com.ward.ward_server.api.user.auth.security.CustomUserDetailService;
import com.ward.ward_server.api.user.auth.security.JwtAuthenticationFilter;
import com.ward.ward_server.api.user.auth.security.UnauthorizedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;
    private final UnauthorizedHandler unauthorizedHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    // Authenticated endpoints
                    authorize.requestMatchers("/auth/logout").authenticated();

                    // User endpoints
                    authorize
                            .requestMatchers("/items/details").hasAnyRole("USER", "ADMIN")
                            .requestMatchers(HttpMethod.GET, "/items").hasAnyRole("USER", "ADMIN");

                    // Public endpoints
                    authorize
                            .requestMatchers("/", "/auth/**", "release-infos/**", "/v1/wc/**", "/items/top10", "/items/{itemId}/details").permitAll()
                            .requestMatchers(HttpMethod.GET, "/brands", "/brands/recommended").permitAll()
                            .requestMatchers(HttpMethod.PATCH, "/brands/{brandId}/view-counts").permitAll()
                            .requestMatchers(HttpMethod.GET, "/release-infos", "/release-infos/details").permitAll();

                    // Admin endpoints
                    authorize
                            .requestMatchers("/items/**").hasRole("ADMIN")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/brands/{brandId}").hasRole("ADMIN")
                            .requestMatchers("/brands/**").hasRole("ADMIN")
                            .requestMatchers("/draw-platforms/**").hasRole("ADMIN")
                            .requestMatchers("/release-infos/**").hasRole("ADMIN");

                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(unauthorizedHandler);
                    exception.accessDeniedHandler(accessDeniedHandler);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", // 웹 개발자 로컬 환경
                "http://localhost:8000", // 기존 설정 유지
                "https://your-web-domain.com", // 실제 웹 애플리케이션 도메인
                "capacitor://your-ios-app", // iOS 앱
                "https://your-android-app" // Android 앱
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Cache-Control"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Preflight request의 최대 유효 시간
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
