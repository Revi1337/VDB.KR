package com.revi1337.service;

import com.revi1337.domain.UserAccount;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.exception.UserNotFoundException;
import com.revi1337.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Module Service
 */
@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public Page<UserAccountDto> findAllUserAccount(Pageable pageable) {
        return userAccountRepository.findAll(pageable)
                .map(UserAccountDto::from);
    }

    public UserAccountDto findUserAccountById(Long userId) {
        return userAccountRepository
                .findById(userId)
                .map(UserAccountDto::from)
                .orElseThrow(UserNotFoundException::new);
    }

    public Optional<UserAccount> findUserAccountByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Transactional
    public void saveUserAccount(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    @Transactional
    public void enableUserAccount(String email) {
        userAccountRepository.enableUserAccount(email);
    }

}
