package org.example.authservice.auth.requests;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ResetPasswordRequest {
    @NonNull
    @Length(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "must contain at least one uppercase letter, one lowercase letter, and one digit.")
    private String password;
}
