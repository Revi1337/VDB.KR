package com.revi1337.config.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration @EnableJpaAuditing
public class JPAConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable("revi1337");
    }
}
