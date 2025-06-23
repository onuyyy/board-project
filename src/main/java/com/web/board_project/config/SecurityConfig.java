package com.web.board_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // 시큐리티 5.7 이상부턴 url 권한 설정 명확히 해줘야 동작
        http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/articles/**").permitAll()
                        .anyRequest().permitAll() // anyRequest (이외 요청)는 authenticated (인증된 사용자만) 접근 가능
                );

        // 시큐리티 5.7 이상부터는 기본 설정 이용 시 자동 로그인 폼 생성 안 됨
        // 아래처럼 명시적어로 설정해야 로그인 폼 활성화됨
        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

}
