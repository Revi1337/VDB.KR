package com.revi1337.service;

import com.revi1337.domain.UserAccount;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.exception.DuplicateEmailException;
import com.revi1337.exception.UserNotFoundException;
import com.revi1337.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional
    public void joinUser(UserAccountDto userAccountDto) {
        UserAccount userAccount = userAccountDto.toEntity();
        String email = userAccount.getEmail();
        if (userAccountRepository.findByEmail(email).isPresent())
            throw new DuplicateEmailException();
        userAccountRepository.save(userAccount);
    }

    public Page<UserAccountDto> findAllUsers(Pageable pageable) {
        return userAccountRepository.findAll(pageable)
                .map(UserAccountDto::from);
    }

    public UserAccountDto findUserById(Long userId) {
        return userAccountRepository
                .findById(userId)
                .map(UserAccountDto::from)
                .orElseThrow(UserNotFoundException::new);
    }

}
