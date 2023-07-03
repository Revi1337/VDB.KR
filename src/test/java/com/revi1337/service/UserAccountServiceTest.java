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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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

    @Test
    @DisplayName(value = "[SERVICE : 전체 회원조회 테스트]")
    public void findAllUserTest() {
        // given
        PageRequest pageRequest = PageRequest.of(1, 10);
        given(userAccountRepository.findAll(pageRequest)).willReturn(Page.empty());

        // when
        Page<UserAccountDto> userAccountDtos = userAccountService.findAllUsers(pageRequest);

        // then
        assertThat(userAccountDtos).isEmpty();
        then(userAccountRepository).should().findAll(pageRequest);
    }


}