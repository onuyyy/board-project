package com.web.board_project.dto.security;

import com.web.board_project.dto.UserAccountDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2Attribute
) implements UserDetails, OAuth2User {

    public static BoardPrincipal of(String username, String password,String email, String nickname, String memo) {
        return of(username, password, email, nickname, memo, Map.of());
    }

    public static BoardPrincipal of(String username, String password,String email, String nickname, String memo, Map<String, Object> oAuth2Attribute) {

        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo,
                oAuth2Attribute
        );
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }

    // Principal -> Dto : 이미 인스턴스 만들어져 있으니까 static x
    public UserAccountDto toDto() {
        return UserAccountDto.of(
                username,
                password,
                email,
                nickname,
                memo
        );
    }


    @Override
    public Map<String, Object> getAttributes() {
        // OAuth 각종 인증 정보를 받는 구조가 다 다를 것이기 때문에 object로 받기 위하여 map
        return oAuth2Attribute;
    }

    @Override
    public String getName() {
        // 유저 식별 정보를 내려준다
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // user credential 이 만료되었는지 계정이 잠겼는지 등의 메서드라
    // 현 프로젝트에서는 관련 설정을 하지 않았기 때문에 true로 반환해도 괜찮다
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Getter
    public enum RoleType {
        USER("ROLE_USER");

        public final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}
