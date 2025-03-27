package com.assesment.users.domain;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import com.assesment.users.domain.spi.UserPersistencePort;
import com.assesment.users.domain.usecase.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserPersistencePort userPersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;
    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("3e8f2c9b-7d42-4a1d-b89f-6c5a7f3e2d51");
        user.setEmail("ricky@example.com");
        user.setFirstName("Ricky");
        user.setLastName("Shen");
        user.setPassword("Password123?");
        user.setRole("ROLE_USER");

        authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setAccessToken("mockAccessToken");
        authenticatedUser.setRefreshToken("mockRefreshToken");
        authenticatedUser.setExpiresIn(3600);
        authenticatedUser.setType("Bearer");

    }

    @Test
    void shouldSaveUserSuccessfully() {
        //Arrange
        when(userPersistencePort.saveUser(user)).thenReturn(user);

        //Act
        User result = userUseCase.save(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getRole(), result.getRole());
        verify(userPersistencePort).saveUser(user);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        //Arrange
        when(userPersistencePort.authenticateUser(user)).thenReturn(authenticatedUser);
        // Act
        AuthenticatedUser result = userUseCase.authenticateUser(user);

        // Assert
        verify(userPersistencePort).authenticateUser(user);
        assertNotNull(result);
        assertEquals(authenticatedUser.getAccessToken(), result.getAccessToken());
        assertEquals(authenticatedUser.getRefreshToken(), result.getRefreshToken());
        assertEquals(authenticatedUser.getExpiresIn(), result.getExpiresIn());
        assertEquals(authenticatedUser.getType(), result.getType());
    }

}
