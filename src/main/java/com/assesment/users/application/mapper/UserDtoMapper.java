package com.assesment.users.application.mapper;

import com.assesment.users.application.dto.request.SuccessfulSignIn;
import com.assesment.users.application.dto.request.SuccessfulSignup;
import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserDtoMapper {
    User toModel(UserSignupDto userSignupDto);
    User signUpToModel(UserSigninDto userSigninDto);
    SuccessfulSignup toSignupResponse(User user);
    SuccessfulSignIn toSigninResponse(AuthenticatedUser user);
}
