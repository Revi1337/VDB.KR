package com.revi1337.controller;

import com.revi1337.dto.UserAccountDto;
import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.response.UserAccountResponse;
import com.revi1337.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequestMapping("/api/v1")
@RestController @RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/users")
    public ResponseEntity<?> findAllUsers(@PageableDefault Pageable pageable) {
        List<UserAccountResponse> userAccountResponses = userAccountService
                .findAllUserAccount(pageable)
                .map(UserAccountResponse::from)
                .toList();
        APIResponse<List<UserAccountResponse>> apiResponse = APIResponse.of(userAccountResponses);
        return ResponseEntity
                .status(OK)
                .body(apiResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable Long userId) {
        UserAccountDto userAccountDto = userAccountService.findUserAccountById(userId);
        UserAccountResponse userAccountResponse = UserAccountResponse.from(userAccountDto);
        APIResponse<UserAccountResponse> apiResponse = APIResponse.of(userAccountResponse);
        return ResponseEntity
                .status(OK)
                .body(apiResponse);
    }

}
