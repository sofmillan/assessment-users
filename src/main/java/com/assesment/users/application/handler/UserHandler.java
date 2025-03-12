package com.assesment.users.application.handler;

import com.assesment.users.application.dto.request.SuccessfulSignIn;
import com.assesment.users.application.dto.request.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;

public interface UserHandler {
    SuccessfulSignup userSignup(UserSignupDto userSignupDto);
    SuccessfulSignIn userSignin(UserSigninDto userSigninDto);
}
