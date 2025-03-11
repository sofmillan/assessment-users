package com.assesment.users.infrastructure.output;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.assesment.users.domain.model.User;
import com.assesment.users.domain.spi.UserPersistencePort;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserRepository implements UserPersistencePort {
    private final DynamoDbTable<UserEntity> userTable;
    private final UserEntityMapper userEntityMapper;
    private final CognitoService cognitoService;

    public UserRepository( DynamoDbTable<UserEntity> userTable, UserEntityMapper userEntityMapper, CognitoService cognitoService) {
        this.userTable = userTable;

        this.userEntityMapper = userEntityMapper;
        this.cognitoService = cognitoService;
    }

    @Override
    public void saveUser(User user) {
        UserEntity entity = userEntityMapper.toEntity(user);
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedAt(LocalDateTime.now());
        userTable.putItem(entity);
        cognitoService.registerUser(user.getEmail(), user.getPassword());
    }
}
