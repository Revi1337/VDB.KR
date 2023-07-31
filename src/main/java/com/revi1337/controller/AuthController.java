package com.revi1337.controller;

import com.revi1337.dto.request.JoinRequest;
import com.revi1337.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/auth")
@RestController @RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody JoinRequest joinRequest,
                         HttpServletRequest httpServletRequest) {
        authenticationService.register(joinRequest.toDto(), httpServletRequest);
    }

    @GetMapping("/register/confirm")                        // TODO 이메일 인증코드가 만료되었는지 등등 추가적인 작업이 필요하다.~
    public String confirmToken(@RequestParam String token) {
        authenticationService.confirmToken(token);
        return "ok";
    }

}
