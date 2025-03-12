package com.assesment.users.infrastructure.output.dynamodb;

import com.assesment.users.domain.model.User;
import com.assesment.users.domain.spi.UserPersistencePort;
import com.assesment.users.infrastructure.output.identity.IdentityService;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.time.LocalDateTime;

public class UserPersistenceAdapter implements UserPersistencePort {
    private final DynamoDbTable<UserEntity> userTable;
    private final UserEntityMapper userEntityMapper;
    private final IdentityService identityService;

    public UserPersistenceAdapter(DynamoDbTable<UserEntity> userTable, UserEntityMapper userEntityMapper, IdentityService identityService) {
        this.userTable = userTable;
        this.userEntityMapper = userEntityMapper;
        this.identityService = identityService;
    }

    @Override
    public User saveUser(User user) {
        String id = identityService.registerUser(user);
        UserEntity entity = userEntityMapper.toEntity(user);
        entity.setId(id);
        entity.setCreatedAt(LocalDateTime.now());
        userTable.putItem(entity);
        return userEntityMapper.toModel(entity);
    }
}
