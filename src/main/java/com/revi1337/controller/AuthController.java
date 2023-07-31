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

    @GetMapping("/email/confirm")
    public String confirmToken(@RequestParam String token) {
        authenticationService.confirmToken(token);
        return "ok";
    }

}
