package com.travellog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
public class ScryptPasswordEncoder implements PasswordEncoder{

    private static final SCryptPasswordEncoder encoder =
            new SCryptPasswordEncoder(
                    16384, // cpuCost: 2^14
                    8,     // memoryCost
                    1,     // parallelization
                    32,    // keyLength
                    64     // saltLength
            );

    @Override
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encrpytedPassword) {
        return encoder.matches(rawPassword, encrpytedPassword);
    }
}
