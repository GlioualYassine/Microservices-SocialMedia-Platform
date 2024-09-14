package org.example.authservice.auth.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class RegistraitonRequest {
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @Size(min = 8 , message = "Password should be at least 8 characters")
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
