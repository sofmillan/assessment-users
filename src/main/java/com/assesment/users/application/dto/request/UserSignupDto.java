package com.assesment.users.application.dto.request;

import com.assesment.users.domain.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserSignupDto {
    @NotNull(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Role is required")
    @Pattern(regexp = "ROLE_HOST|ROLE_USER", message = "Role is not valid")
    private String role;
}
