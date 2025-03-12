package com.assesment.users.application.dto.request;

import lombok.Data;

@Data
public class UserSigninDto {
    private String email;
    private String password;
}
