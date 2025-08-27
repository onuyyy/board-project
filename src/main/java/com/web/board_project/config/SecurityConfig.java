package com.web.board_project.config;

import com.web.board_project.dto.UserAccountDto;
import com.web.board_project.dto.security.BoardPrincipal;
import com.web.board_project.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // 시큐리티 5.7 이상부턴 url 권한 설정 명확히 해줘야 동작
        http.authorizeHttpRequests(auth -> auth
                        // PathRequest 모든 로케이션 중 스프링에서 매칭해놓은 스태틱 리소스들을 미리 담아두었다
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/","/articles","/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated() // anyRequest (이외 요청)는 authenticated (인증된 사용자만) 접근 가능
                );

        // 시큐리티 5.7 이상부터는 기본 설정 이용 시 자동 로그인 폼 생성 안 됨
        // 아래처럼 명시적어로 설정해야 로그인 폼 활성화됨
        http.formLogin(Customizer.withDefaults());
        http.logout(logout -> logout
                .logoutSuccessUrl("/"));

        return http.build();
    }

    // 사용자 인증 정보를 가져온다
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. - username : " + username));
    }

    // 패스워드 인코더를 구현
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
