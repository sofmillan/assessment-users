package com.assesment.users.domain.usecase;

import com.assesment.users.domain.api.UserServicePort;
import com.assesment.users.domain.model.User;
import com.assesment.users.domain.spi.UserPersistencePort;

public class UserUseCase implements UserServicePort {
    private final UserPersistencePort userPersistencePort;

    public UserUseCase(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void save(User user) {
        userPersistencePort.saveUser(user);
    }
}
