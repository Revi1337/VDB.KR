package com.revi1337.service;

import com.revi1337.domain.UserAccount;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName(value = "[Serivce : Service Slice Test]")
@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @InjectMocks private UserAccountService userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    @DisplayName(value = "[SERVICE : 회원가입 테스트]")
    public void joinUser() {
        // given
        UserAccountDto userAccountDto = UserAccountDto.of(null, "revi1337@naver.com", "revi1337", "1337");
        UserAccount userAccount = userAccountDto.toEntity();
        given(userAccountRepository.save(any())).willReturn(userAccount);
        given(userAccountRepository.findById(1L)).willReturn(Optional.of(userAccount));

        // when
        userAccountService.joinUser(userAccountDto);

        // then
        UserAccount findUserAccount = userAccountRepository.findById(1L).get();
        assertThat(userAccount.getId()).isEqualTo(findUserAccount.getId());
    }
}