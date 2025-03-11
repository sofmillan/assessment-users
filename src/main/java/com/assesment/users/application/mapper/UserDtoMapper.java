package com.assesment.users.application.mapper;

import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserDtoMapper {
    User toModel(UserSignupDto userSigninDto);
}
