package chatapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity @Builder
@Table(
        name = "_user",
        uniqueConstraints = @UniqueConstraint(
                name = "constraint_unique_email",
                columnNames = "email"
        )
)
public class User {
    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;
    private boolean isOnline = false;
}
