package com.assesment.users.application.dto.request;

import lombok.Data;

@Data
public class SuccessfulSignIn {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String type;
}
