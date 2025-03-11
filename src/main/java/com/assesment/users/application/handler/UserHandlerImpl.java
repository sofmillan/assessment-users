package com.assesment.users.application.handler;

import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.mapper.UserDtoMapper;
import com.assesment.users.domain.api.UserServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandlerImpl implements UserHandler{
    private final UserServicePort userServicePort;
    private final UserDtoMapper userDtoMapper;
    @Override
    public void userSignup(UserSignupDto userSignupDto) {
        userServicePort.save(userDtoMapper.toModel(userSignupDto));
    }
}
