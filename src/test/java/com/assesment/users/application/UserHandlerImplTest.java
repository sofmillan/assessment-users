package com.assesment.users.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.assesment.users.application.dto.request.UserSigninDto;
import com.assesment.users.application.dto.request.UserSignupDto;
import com.assesment.users.application.dto.response.SuccessfulSignIn;
import com.assesment.users.application.dto.response.SuccessfulSignup;
import com.assesment.users.application.handler.UserHandlerImpl;
import com.assesment.users.application.mapper.UserDtoMapper;
import com.assesment.users.domain.api.UserServicePort;
import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserHandlerImplTest {
    @Mock
    private UserServicePort userServicePort;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private UserHandlerImpl userHandler;

    private UserSignupDto userSignupDto;
    private User user;
    private SuccessfulSignup successfulSignup;

    private UserSigninDto userSigninDto;
    private AuthenticatedUser authenticatedUser;
    private SuccessfulSignIn successfulSignIn;

    @BeforeEach
    void setUp() {
        userSignupDto = new UserSignupDto();
        userSignupDto.setEmail("test@example.com");
        userSignupDto.setPassword("Test@1234");
        userSignupDto.setFirstName("John");
        userSignupDto.setLastName("Doe");
        userSignupDto.setRole("ROLE_USER");

        user = new User();
        user.setId("12345");
        user.setEmail(userSignupDto.getEmail());
        user.setFirstName(userSignupDto.getFirstName());
        user.setLastName(userSignupDto.getLastName());
        user.setPassword(userSignupDto.getPassword());
        user.setRole(userSignupDto.getRole());
    }

    @Test
    void shouldSignupUserSuccessfully() {

        successfulSignup = new SuccessfulSignup();
        successfulSignup.setFirstName(user.getFirstName());
        successfulSignup.setLastName(user.getLastName());
        successfulSignup.setEmail(user.getEmail());

        userSigninDto = new UserSigninDto();
        userSigninDto.setEmail(userSignupDto.getEmail());
        userSigninDto.setPassword(userSignupDto.getPassword());

        when(userDtoMapper.toModel(userSignupDto)).thenReturn(user);
        when(userServicePort.save(user)).thenReturn(user);
        when(userDtoMapper.toSignupResponse(user)).thenReturn(successfulSignup);

        // Act
        SuccessfulSignup result = userHandler.userSignup(userSignupDto);

        // Assert
        assertNotNull(result);
        assertEquals(successfulSignup.getFirstName(), result.getFirstName());
        assertEquals(successfulSignup.getLastName(), result.getLastName());
        assertEquals(successfulSignup.getEmail(), result.getEmail());
        verify(userDtoMapper).toModel(userSignupDto);
        verify(userServicePort).save(user);
        verify(userDtoMapper).toSignupResponse(user);
    }

    @Test
    void shouldSigninUserSuccessfully() {
        authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setAccessToken("eyJhbGciOiJIUzIXVCJ9.eyJzdWIiOiIxMjM0NDkwIiwibmFtZSkpvaoxN.SflKxwRJKF2QT4fwpMeJf");
        authenticatedUser.setRefreshToken("eyJhbGciOiJIUzIXVCJ9.eyJzdWIwIiwsjM0NDkwIiwibmFtZSkpvaoxN.SflKT4JKF2QT4fRJKF2Qf");
        authenticatedUser.setExpiresIn(3600);
        authenticatedUser.setType("Bearer");

        successfulSignIn = new SuccessfulSignIn();
        successfulSignIn.setAccessToken(authenticatedUser.getAccessToken());
        successfulSignIn.setRefreshToken(authenticatedUser.getRefreshToken());
        successfulSignIn.setExpiresIn(authenticatedUser.getExpiresIn());
        successfulSignIn.setType(authenticatedUser.getType());


        when(userDtoMapper.signUpToModel(userSigninDto)).thenReturn(user);
        when(userServicePort.authenticateUser(user)).thenReturn(authenticatedUser);
        when(userDtoMapper.toSigninResponse(authenticatedUser)).thenReturn(successfulSignIn);

        // Act
        SuccessfulSignIn result = userHandler.userSignin(userSigninDto);

        // Assert
        assertNotNull(result);
        assertEquals(successfulSignIn.getAccessToken(), result.getAccessToken());
        assertEquals(successfulSignIn.getRefreshToken(), result.getRefreshToken());
        assertEquals(successfulSignIn.getExpiresIn(), result.getExpiresIn());
        assertEquals(successfulSignIn.getType(), result.getType());
        verify(userDtoMapper).signUpToModel(userSigninDto);
        verify(userServicePort).authenticateUser(user);
        verify(userDtoMapper).toSigninResponse(authenticatedUser);
    }
}
