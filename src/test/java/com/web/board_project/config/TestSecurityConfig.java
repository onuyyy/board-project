package com.web.board_project.config;

import com.web.board_project.service.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;

@Configuration
public class TestSecurityConfig {

    @MockBean
    private UserAccountService userAccountService;

    public TestSecurityConfig() {
        given(userAccountService.searchUser(anyString()))
                .willReturn(Optional.of(createTestUserAccountDto()));
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .formLogin();
        return http.build();
    }

    private static com.web.board_project.dto.UserAccountDto createTestUserAccountDto() {
        return com.web.board_project.dto.UserAccountDto.of(
                "testId",
                "pw",
                "onuy@email.com",
                "test",
                "test memo"
        );
    }
}
