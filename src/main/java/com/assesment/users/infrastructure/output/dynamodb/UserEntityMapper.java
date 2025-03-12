package com.assesment.users.infrastructure.output.dynamodb;

import com.assesment.users.domain.model.User;
import com.assesment.users.infrastructure.output.dynamodb.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {
    UserEntity toEntity(User user);
    User toModel(UserEntity user);
}
