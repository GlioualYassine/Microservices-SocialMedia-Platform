package org.example.userservice.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    // Liste des IDs de likes associés au post
    @ElementCollection
    @CollectionTable(name = "friends_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friends_ids")
    private List<UUID> friends = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "friendsRequest_sent_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friends_request_sent_ids")
    private List<UUID> friendsRequestSent = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "friendsRequest_received_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "friends_request_received_ids")
    private List<UUID> friendsRequestReceived = new ArrayList<>();

}
