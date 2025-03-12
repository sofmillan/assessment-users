package com.assesment.users.domain.api;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;

public interface UserServicePort {
    User save(User user);
    AuthenticatedUser authenticateUser(User user);
}
