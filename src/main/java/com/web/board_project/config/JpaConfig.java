package com.web.board_project.config;

import com.web.board_project.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static java.util.Locale.filter;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    /*
        AuditorAware는 사용자 정보가 필요하여 security 이용해서 사용자 정보 가져온다
        로그인 유저 정보만 가져와서
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
                    // securityContextHolder라는 security에 대한 정보를 모두 들고 있는 놈 > getContext security Context 가져옴
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(BoardPrincipal.class::cast)
                .map(BoardPrincipal::getUsername);
    }
}
