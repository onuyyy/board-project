package com.web.board_project.config;

import com.web.board_project.domain.UserAccount;
import com.web.board_project.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import({SecurityConfig.class}) // 정의되어 있는 설정들 바로 쓰기 위하여
public class TestSecurityConfig {

    @MockBean
    private UserAccountRepository userAccountRepository;

    @BeforeTestMethod // spring이 지원해주는 spring test를 한다면, 특정 주기에 맞춰서 이 메서드 호출하겠음
    public void securitySetup() {
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "testId",
                "pw",
                "onuy@email.com",
                "test",
                "test memo"
        )));
    }
}
