package com.revi1337.controller;

import com.revi1337.dto.request.JoinRequest;
import com.revi1337.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1")
@RestController @RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping("/user/join")
    public void joinUser(@RequestBody @Valid JoinRequest joinRequest) {
        userAccountService.joinUser(joinRequest.toDto());
    }

}
