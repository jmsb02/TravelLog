package com.travellog.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TravelLogMockSecurityContext.class)
public @interface TravelLogMockUser {


    String name() default "TravelLog";

    String email() default "jmmmm@naver.com";

    String password() default "";

//    String role() default "ROLE_ADMIN";

}
