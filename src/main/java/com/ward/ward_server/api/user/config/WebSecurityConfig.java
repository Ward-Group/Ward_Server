package com.ward.ward_server.api.user.config;

import com.ward.ward_server.api.user.auth.security.CustomUserDetailService;
import com.ward.ward_server.api.user.auth.security.JwtAuthenticationFilter;
import com.ward.ward_server.api.user.auth.security.UnauthorizedHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;
    private final UnauthorizedHandler unauthorizedHandler;

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {

        http
//                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**") // map current config to given resource path

                .formLogin(AbstractHttpConfigurer::disable)
                    .exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler)
                    .and()

                //cors 설정
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8000")); // 프론트 주소(또 바꿔야하는 곳:CorsMvcConfig,CustomSuccessHandler)
                        configuration.setAllowedMethods(Collections.singletonList("*")); // 모든 요청에 허용
                        configuration.setAllowCredentials(true); //
                        configuration.setAllowedHeaders(Collections.singletonList("*")); // 어떤 헤더값 받을지 세팅
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie")); // 서버에서 데이터 준 걸 웹페이지에서 보이게 하려면
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }))

                //JWTFilter 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 세션 관리 정책 설정 - 상태를 유지하지 않는 세션
                .sessionManagement(sessionManagementConfigurer
                        -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 경로별 인가 작업
                .authorizeHttpRequests(registry -> registry // 요청에 대한 권한 설정 메서드
                        //.requestMatchers("/", "/auth/**", "/v1/wc/**", "/item/**").permitAll() // / 경로 요청에 대한 권한을 설정. permitAll() 모든 사용자, 인증되지않은 사용자에게 허용
                        //.requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 에게만 허용
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated() // 다른 나머지 모든 요청에 대한 권한 설정, authenticated()는 인증된 사용자에게만 허용, 로그인해야만 접근 가능
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }
}