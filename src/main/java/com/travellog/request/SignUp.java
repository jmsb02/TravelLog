package com.travellog.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUp {

    private String name;
    private String password;
    private String email;

}
