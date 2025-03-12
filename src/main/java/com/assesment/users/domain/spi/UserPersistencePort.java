package com.assesment.users.domain.spi;

import com.assesment.users.domain.model.User;

public interface UserPersistencePort {
    User saveUser(User user);
}
