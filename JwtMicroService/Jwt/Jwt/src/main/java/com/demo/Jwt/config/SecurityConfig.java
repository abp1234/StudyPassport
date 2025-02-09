package com.demo.Jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 폼 로그인 비활성화 (무상태 API의 경우 필요하지 않음)
                .formLogin(form -> form.disable())
                .csrf(csrf->csrf.disable());
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
