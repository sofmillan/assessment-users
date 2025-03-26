package com.assesment.users.application.handler;

import com.assesment.users.application.dto.response.SuccessfulSignIn;
import com.assesment.users.application.dto.response.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.mapper.UserDtoMapper;
import com.assesment.users.domain.api.UserServicePort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHandlerImpl implements UserHandler{
    private final UserServicePort userServicePort;
    private final UserDtoMapper userDtoMapper;
    @Override
    public SuccessfulSignup userSignup(UserSignupDto userSignupDto) {
        return userDtoMapper.toSignupResponse(userServicePort.save(userDtoMapper.toModel(userSignupDto)));
    }

    @Override
    public SuccessfulSignIn userSignin(UserSigninDto userSigninDto) {
        return userDtoMapper.toSigninResponse(userServicePort.authenticateUser(userDtoMapper.signUpToModel(userSigninDto)));
    }
}
