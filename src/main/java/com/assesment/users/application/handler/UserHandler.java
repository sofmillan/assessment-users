package com.assesment.users.application.handler;

import com.assesment.users.application.dto.request.UserSignupDto;

public interface UserHandler {
    void userSignup(UserSignupDto userSigninDto);
}
