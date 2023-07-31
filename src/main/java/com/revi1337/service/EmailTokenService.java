package com.revi1337.service;

import com.revi1337.domain.EmailToken;
import com.revi1337.exception.EmailTokenNotFoundException;
import com.revi1337.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Module Service
 */
@Service @RequiredArgsConstructor @Transactional
public class EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;

    public void saveEmailToken(EmailToken emailToken) {
        emailTokenRepository.save(emailToken);
    }

    public void confirmToken(String token, LocalDateTime confirmedAt) {
        emailTokenRepository.updateEmailTokenConfirm(token, confirmedAt);
    }

    @Transactional(readOnly = true)
    public EmailToken getEmailToken(String token) {
        return emailTokenRepository.findByToken(token)
                .orElseThrow(EmailTokenNotFoundException::new);
    }

}