package com.assesment.users.application.handler;

import com.assesment.users.application.dto.request.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSignupDto;

public interface UserHandler {
    SuccessfulSignup userSignup(UserSignupDto userSigninDto);
}
