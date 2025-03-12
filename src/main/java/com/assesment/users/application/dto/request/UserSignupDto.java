package com.assesment.users.application.dto.request;

import lombok.Data;

@Data
public class UserSignupDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
}
