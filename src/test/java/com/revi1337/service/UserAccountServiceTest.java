package com.revi1337.service;

import com.revi1337.domain.UserAccount;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.exception.UserNotFoundException;
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
import static org.assertj.core.api.Assertions.catchThrowable;
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

    @Test
    @DisplayName(value = "[SERVICE : userId 로 UserAccount 를 조회하면 UserAccountResponse 를 반환한다.]")
    public void mustBeResponseWhenRequestExistingUserAccountId() {
        // given
        Long userId = 1L;
        UserAccountDto userAccountDto = UserAccountDto.of(userId, "revi1337@naver.com", "revi1337", "1337");
        UserAccount userAccount = userAccountDto.toEntity();
        given(userAccountRepository.findById(userId)).willReturn(Optional.of(userAccount));

        // When
        UserAccountDto userAccountDto1 = userAccountService.findUserById(userId);

        // Then
        assertThat(userAccountDto1)
                .hasFieldOrPropertyWithValue("email", userAccountDto1.email())
                .hasFieldOrPropertyWithValue("username", userAccountDto1.username())
                .hasFieldOrPropertyWithValue("password", userAccountDto1.password());
        then(userAccountRepository).should().findById(userId);
    }

    @Test
    @DisplayName(value = "[SERVICE : 존재하지않는 userId 로 UserAccount 를 조회하면 익셉션을 던진다.]")
    public void mustBeExceptionWhenRequestNonExistingUserAccountId() {
        // given
        Long userId = 1L;
        given(userAccountRepository.findById(userId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> userAccountService.findUserById(userId));

        // Then
        assertThat(t)
                .isInstanceOf(UserNotFoundException.class);
        then(userAccountRepository).should().findById(userId);
    }


}