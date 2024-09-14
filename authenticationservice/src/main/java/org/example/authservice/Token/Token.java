package org.example.authservice.Token;

import jakarta.persistence.*;
import lombok.*;
import org.example.authservice.models.User;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity @Builder


public class Token {

    @Id
    @GeneratedValue
    private Integer Id;
    private String token;

    private LocalDateTime createdDate;
    private LocalDateTime expiresDate;
    private LocalDateTime validatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}
