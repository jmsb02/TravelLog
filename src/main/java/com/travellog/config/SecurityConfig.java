package com.travellog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travellog.config.filter.EmailPasswordAuthFilter;
import com.travellog.config.handler.Http401Handler;
import com.travellog.config.handler.Http403Handler;
import com.travellog.config.handler.LoginFailHandler;
import com.travellog.config.handler.LoginSuccessHandler;
import com.travellog.domain.User;
import com.travellog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                web.ignoring()
                        .requestMatchers("/favicon.ico", "/error")
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        .requestMatchers(toH2Console());
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll()
                )
                .addFilterBefore(usernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe((rm) -> rm.rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(86400) // 1일(초 단위)
                )
                .exceptionHandling(e -> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                })
                .build();
    }

    @Bean
    public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(userRepository));
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsService() {

            //username(아이디) 기반으로 유저 조회, 우리는 아이디를 email로 했기 때문에 findByEmail 사용
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(username + "를 찾을 수 없습니다."));

                return new UserPrincipal(user);
            }
        };
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder(
                16384, // cpuCost: 2^14
                8,     // memoryCost
                1,     // parallelization
                32,    // keyLength
                64     // saltLength
        );
    }

}
