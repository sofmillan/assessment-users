package com.assesment.users.infrastructure.input.rest;

import com.assesment.users.application.dto.response.SuccessfulSignIn;
import com.assesment.users.application.dto.response.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.handler.UserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserHandler userHandler;
    @PostMapping("/signup")
    public SuccessfulSignup signUp(@Valid @RequestBody UserSignupDto user) {
        return userHandler.userSignup(user);
    }

    @PostMapping("/signin")
    public SuccessfulSignIn signIn(@Valid @RequestBody UserSigninDto user) {
        return userHandler.userSignin(user);
    }
}
