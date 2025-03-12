package com.assesment.users.domain.spi;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;

public interface UserPersistencePort {
    User saveUser(User user);
    AuthenticatedUser authenticateUser(User user);
}
