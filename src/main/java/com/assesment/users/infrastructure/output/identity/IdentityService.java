package com.assesment.users.infrastructure.output.identity;

import com.assesment.users.domain.model.AuthenticatedUser;
import com.assesment.users.domain.model.User;

public interface IdentityService {
    String registerUser(User user);
    AuthenticatedUser loginUser(User user);
}
