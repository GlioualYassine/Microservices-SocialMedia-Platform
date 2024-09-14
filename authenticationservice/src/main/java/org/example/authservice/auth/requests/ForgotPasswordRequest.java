package org.example.authservice.auth.requests;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class ForgotPasswordRequest {
    @Email
    private String email ;
}
