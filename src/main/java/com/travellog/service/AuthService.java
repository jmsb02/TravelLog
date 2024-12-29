package com.travellog.service;

import com.travellog.crypto.PasswordEncoder;
import com.travellog.domain.User;
import com.travellog.exception.AlreadyExistsEmailException;
import com.travellog.repository.UserRepository;
import com.travellog.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    public void signup(@RequestBody SignUp signUp) {
        Optional<User> userOptional = userRepository.findByEmail(signUp.getEmail());
        if(userOptional.isPresent()) {
            throw  new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signUp.getPassword());

        User user = User.builder()
                .name(signUp.getName())
                .password(encryptedPassword)
                .email(signUp.getEmail())
                .build();
        userRepository.save(user);
    }
}
