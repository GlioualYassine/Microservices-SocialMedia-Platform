package org.example.authservice.auth.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class PasswordResetResponse {
    private String fullName;
    private String email;
}
