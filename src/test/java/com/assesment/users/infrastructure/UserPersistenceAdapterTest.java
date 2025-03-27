package com.assesment.users.infrastructure;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import com.assesment.users.infrastructure.output.dynamodb.UserEntity;
import com.assesment.users.infrastructure.output.dynamodb.UserEntityMapper;
import com.assesment.users.infrastructure.output.dynamodb.UserPersistenceAdapter;
import com.assesment.users.infrastructure.output.identity.IdentityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock
    private DynamoDbTable<UserEntity> userTable;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private IdentityService identityService;

    @InjectMocks
    private UserPersistenceAdapter userPersistenceAdapter;
    private User user;
    private UserEntity userEntity;
    private String generatedUserId;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("ricky@example.com");
        user.setFirstName("Ricky");
        user.setLastName("Shen");
        user.setPassword("Password123?");
        user.setRole("ROLE_HOST");

        generatedUserId = "3e8f2c9b-7d42-4a1d-b89f-6c5a7f3e2d51";

        userEntity = new UserEntity();
        userEntity.setId(generatedUserId);
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldSaveUserSuccessfully() {
        //Arrange
        when(identityService.registerUser(user)).thenReturn(generatedUserId);
        when(userEntityMapper.toEntity(user)).thenReturn(userEntity);
        when(userEntityMapper.toModel(userEntity)).thenReturn(user);

        // Act
        User savedUser = userPersistenceAdapter.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        verify(identityService).registerUser(user);
        verify(userEntityMapper).toEntity(user);
        verify(userTable).putItem(userEntity);
        verify(userEntityMapper).toModel(userEntity);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setAccessToken("mockAccessToken");
        authenticatedUser.setRefreshToken("mockRefreshToken");
        authenticatedUser.setExpiresIn(3600);
        authenticatedUser.setType("Bearer");

        when(identityService.loginUser(user)).thenReturn(authenticatedUser);

        // Act
        AuthenticatedUser result = userPersistenceAdapter.authenticateUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(authenticatedUser.getAccessToken(), result.getAccessToken());
        assertEquals(authenticatedUser.getRefreshToken(), result.getRefreshToken());
        assertEquals(authenticatedUser.getExpiresIn(), result.getExpiresIn());
        assertEquals(authenticatedUser.getType(), result.getType());
        verify(identityService).loginUser(user);

    }
}
