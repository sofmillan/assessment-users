package com.assesment.users.domain.usecase;

import com.assesment.users.domain.api.UserServicePort;
import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;
import com.assesment.users.domain.spi.UserPersistencePort;

public class UserUseCase implements UserServicePort {
    private final UserPersistencePort userPersistencePort;

    public UserUseCase(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public User save(User user) {
        return userPersistencePort.saveUser(user);
    }

    @Override
    public AuthenticatedUser authenticateUser(User user) {
        return userPersistencePort.authenticateUser(user);
    }
}
