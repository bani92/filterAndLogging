package com.example.filterandlogging.common.configs;

import com.example.filterandlogging.common.auth.JwtAuthFilter;
import com.example.filterandlogging.common.auth.XSSFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfigs {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

//    private final JwtAuthFilter jwtAuthFilter;
//
//    public SecurityConfigs(JwtAuthFilter jwtAuthFilter) {
//        this.jwtAuthFilter = jwtAuthFilter;
//    }

    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 비활성화
                .authorizeHttpRequests(a -> a.requestMatchers("/member/create","/member/doLogin").permitAll().anyRequest().authenticated())
                // requestMatchers 안에는 허용, 나머지는 인증처리(Authentication 객체를 요구함)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션방식을 사용하지 않겠다
                .addFilterBefore(new XSSFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더값 허용
        configuration.setAllowCredentials(true); // 자격증명허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 url에 패턴에 대해 cors 허용 설정
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
