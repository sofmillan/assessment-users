package com.assesment.users.infrastructure.input.rest;

import com.assesment.users.application.dto.request.SuccessfulSignIn;
import com.assesment.users.application.dto.request.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserHandler userHandler;
    @PostMapping("/signup")
    public SuccessfulSignup signUp(@RequestBody UserSignupDto user) {
        return userHandler.userSignup(user);
    }

    @PostMapping("/singin")
    public SuccessfulSignIn signIn(@RequestBody UserSigninDto user) {
        return userHandler.userSignin(user);
    }
}
