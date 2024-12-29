package com.travellog.config;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long userId;


    public UserPrincipal(com.travellog.domain.User user) {
        super(user.getEmail(), user.getPassword(), List.of(
                new SimpleGrantedAuthority("ROLE_USER") //역할
        ));
        this.userId = user.getId();
    }

}
