package com.travellog.config;

import com.travellog.domain.User;
import com.travellog.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


public class TravelLogMockSecurityContext implements WithSecurityContextFactory<TravelLogMockUser> {

    private final UserRepository userRepository;

    public TravelLogMockSecurityContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityContext createSecurityContext(TravelLogMockUser annotation) {
        var user = User.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal, user.getPassword(),
                List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
