package com.travellog.service;

import com.travellog.domain.User;
import com.travellog.exception.AlreadyExistsEmailException;
import com.travellog.repository.UserRepository;
import com.travellog.request.SignUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given

        SignUp signUp = SignUp.builder()
                .email("jmmmm@naver.com")
                .password("1234")
                .name("TravelLog")
                .build();

        //when
        authService.signup(signUp);

        //then
        assertEquals(1L, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("jmmmm@naver.com", user.getId());
        assertNotNull(user.getPassword());
        assertEquals("1234", user.getPassword());
        assertEquals("TravelLog", user.getName());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        //given

        User user = User.builder()
                .email("jmmmm@naver.com")
                .password("1234")
                .name("TravelLog")
                .build();

        userRepository.save(user);


        SignUp signUp = SignUp.builder()
                .email("jmmmm@naver.com")
                .password("1234")
                .name("TravelLog")
                .build();

        //Expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signUp));

    }

}