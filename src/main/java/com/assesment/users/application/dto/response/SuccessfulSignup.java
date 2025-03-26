package com.assesment.users.application.dto.response;

import lombok.Data;

@Data
public class SuccessfulSignup {
    private String firstName;
    private String lastName;
    private String email;
}
