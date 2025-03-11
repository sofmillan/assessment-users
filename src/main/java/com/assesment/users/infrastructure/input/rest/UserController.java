package com.assesment.users.infrastructure.input.rest;

import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserHandler userHandler;
    @PostMapping("/register")
    public void registerUser(@RequestBody UserSignupDto user) {
        userHandler.userSignup(user);
    }
}
