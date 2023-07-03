package com.revi1337.controller;

import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.request.JoinRequest;
import com.revi1337.dto.response.UserAccountResponse;
import com.revi1337.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequestMapping("/api/v1")
@RestController @RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping("/user/join")
    public void joinUser(@RequestBody @Valid JoinRequest joinRequest) {
        userAccountService.joinUser(joinRequest.toDto());
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(@PageableDefault Pageable pageable) {
        List<UserAccountResponse> userAccountResponses = userAccountService
                .findAllUsers(pageable)
                .map(UserAccountResponse::from)
                .toList();
        APIResponse<List<UserAccountResponse>> apiResponse = APIResponse.of(userAccountResponses);
        return ResponseEntity
                .status(OK)
                .body(apiResponse);
    }

}
