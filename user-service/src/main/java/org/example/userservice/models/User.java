package org.example.userservice.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Entity
@EntityListeners(AuditingEntityListener.class)@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "_users")
public class User {
    @Id
    private UUID id ;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String bio;
    public Date birthDate;
    @CreatedDate
    @Column(updatable = false , nullable = false)
    LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    LocalDateTime updatedAt;
}
