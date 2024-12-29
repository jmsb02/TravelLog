package com.travellog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellog.repository.UserRepository;
import com.travellog.request.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        //given
        SignUp signUp = SignUp.builder()
                .email("jmmmm@naver.com")
                .password("1234")
                .name("TravelLog")
                .build();

        //expected
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signUp))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}