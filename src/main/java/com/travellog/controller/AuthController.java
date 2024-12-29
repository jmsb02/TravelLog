package com.travellog.controller;

import com.travellog.config.AppConfig;
import com.travellog.request.SignUp;
import com.travellog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/signup")
    public void signUp(@RequestBody SignUp signUp) {
        authService.signup(signUp);
    }

}

