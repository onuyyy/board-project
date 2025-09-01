package com.web.board_project.config;

import com.web.board_project.dto.security.BoardPrincipal;
import com.web.board_project.dto.security.KakaoOAuth2Response;
import com.web.board_project.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
            ) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        // 시큐리티 5.7 이상부턴 url 권한 설정 명확히 해줘야 동작
        http.authorizeHttpRequests(auth -> auth
                        // PathRequest 모든 로케이션 중 스프링에서 매칭해놓은 스태틱 리소스들을 미리 담아두었다
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/", "/articles", "/articles/search-hashtag"
                        ).permitAll()
                        .anyRequest().authenticated() // anyRequest (이외 요청)는 authenticated (인증된 사용자만) 접근 가능
                )
                // 시큐리티 5.7 이상부터는 기본 설정 이용 시 자동 로그인 폼 생성 안 됨
                // 아래처럼 명시적어로 설정해야 로그인 폼 활성화됨
                .formLogin(withDefaults())
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .oauth2Login(oAuth -> oAuth
                        .userInfoEndpoint(userInfo -> userInfo
                                // userDetailService가 했던 것처럼 같은 방식으로 넣어줄 것 #todo
                                .userService(oAuth2UserService)
                        )
                );

        return http.build();
    }

    // 사용자 인증 정보를 가져온다
    @Bean
    public UserDetailsService userDetailsService(UserAccountService userAccountService) {
        return username -> userAccountService
                .searchUser(username)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. - username : " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        // 바깥쪽 return → 전체 oAuth2UserService 메서드 반환값
        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao가 반환될 것
            String providerId = kakaoResponse.id().toString();
            String username = registrationId + "_" + providerId; // kakao_고유값
            String dummyPassword = passwordEncoder.encode(UUID.randomUUID().toString());

            // 안쪽 return → loadUser(userRequest)의 반환값
            // DB 확인 후 있으면 그냥 진행, 없으면 DB에 저장한다
            return userAccountService.searchUser(username)
                    .map(BoardPrincipal::from)
                    .orElseGet(() ->
                            BoardPrincipal.from(
                                    userAccountService.saveUser(
                                            username, // 인증은 카카오에 넘길 것이기 때문에 디비에 패스워드 저장할 필요없는데 null x라 입력
                                            dummyPassword,
                                            kakaoResponse.email(),
                                            kakaoResponse.nickname(),
                                            null
                                    )
                            )
                    );

        };
    }

    // 패스워드 인코더를 구현
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
