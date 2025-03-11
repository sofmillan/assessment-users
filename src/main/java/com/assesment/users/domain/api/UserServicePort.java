package com.assesment.users.domain.api;

import com.assesment.users.domain.model.User;

public interface UserServicePort {
    void save(User user);
}
