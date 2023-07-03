package com.revi1337.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.domain.UserAccount;
import com.revi1337.dto.request.JoinRequest;
import com.revi1337.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName(value = "[Controller : Integrated Test]")
@SpringBootTest @AutoConfigureMockMvc @Transactional @Rollback
class UserAccountControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvc mockMvc;

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountControllerTest(MockMvc mockMvc, UserAccountRepository userAccountRepository) {
        this.mockMvc = mockMvc;
        this.userAccountRepository = userAccountRepository;
    }

    @Test
    @DisplayName(value = "[Controller : 회원가입 테스트]")
    public void joinUserTest() throws Exception {
        JoinRequest joinRequest = new JoinRequest("revi1337@naver.com", "revi1338", "1337");
        String joinRequestJson = objectMapper.writeValueAsString(joinRequest);
        System.out.println(joinRequestJson);

        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(joinRequestJson))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 회원가입 테스트 - 이메일포맷이 아니면 400 을 뱉어야 한다.]")
    public void mustBeExceptionWhenRequestUserJoinWithNonEmailFormat() throws Exception {
        JoinRequest joinRequest = new JoinRequest("sdfas", "revi1338", "1337");
        String joinRequestJson = objectMapper.writeValueAsString(joinRequest);
        System.out.println(joinRequestJson);

        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 회원가입 테스트 - username 필드를 입력하지 않거나 null 이면 400 을 뱉어야 한다.]")
    public void mustBeExceptionWhenRequestUserJoinWithMissingUsername() throws Exception {
        JoinRequest joinRequest = new JoinRequest("revi1337@naver.com", "", "1337");
        String joinRequestJson = objectMapper.writeValueAsString(joinRequest);
        System.out.println(joinRequestJson);

        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.validation.username").value("username must be specified"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 회원가입 테스트 - password 필드를 입력하지 않거나 null 이면 400 을 뱉어야 한다.]")
    public void mustBeExceptionWhenRequestUserJoinWithMissingPassword() throws Exception {
        JoinRequest joinRequest = new JoinRequest("revi1337@naver.com", "revi1337", null);
        String joinRequestJson = objectMapper.writeValueAsString(joinRequest);
        System.out.println(joinRequestJson);

        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.validation.password").value("password must be specified"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 회원가입 테스트 - 동일한 Email 로 회원가입을 시도하면 DuplicateEmailException 이 터져야한다.]")
    public void mustBeExceptionWhenRequestUserJoinWithUsedSameEmail() throws Exception {
        JoinRequest joinRequest = new JoinRequest("revi1337@naver.com", "revi1337", "revi1337");
        String joinRequestJson = objectMapper.writeValueAsString(joinRequest);
        System.out.println(joinRequestJson);

        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("asd")
                .password("asd")
                .build();
        userAccountRepository.save(userAccount);

        mockMvc.perform(post("/api/v1/user/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(joinRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("duplicate email. plz specify other email"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 전체 회원 조회]")
    public void findAllUserTest() throws Exception {
        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("revi1337")
                .password("1337")
                .build();
        userAccountRepository.save(userAccount);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray())
                .andExpect(jsonPath("$.payload.size()").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 특정 ID 를 갖는 UserAccount 조회하면 200 과 json 을 내려준다.]")
    public void findUserByIdTest() throws Exception {
        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("revi1337")
                .password("1337")
                .build();
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);
        Long id = savedUserAccount.getId();

        mockMvc.perform(get("/api/v1/user/{userId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email").value("revi1337@naver.com"))
                .andExpect(jsonPath("$.payload.username").value("revi1337"))
                .andExpect(jsonPath("$.payload.password").value("1337"))
                .andDo(print());
    }

    @Test
    @DisplayName(value = "[Controller : 존재하지않는 ID 를  갖는 UserAccount 조회하면 400 과 Err Json 이 내려온다.]")
    public void returnErrJsonWhenRequestNonExistingUserAccountId() throws Exception {
        mockMvc.perform(get("/api/v1/user/{userId}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.statusCode").value(404))
                .andExpect(jsonPath("$.error.message").value("user not found"))
                .andDo(print());
    }

}