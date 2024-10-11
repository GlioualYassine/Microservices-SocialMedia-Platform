package org.example.friendshipservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Node
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id;

    private String username;
    private String email;

    @Relationship(type = "FRIENDSHIP", direction = Relationship.Direction.OUTGOING)
    private Set<Friendship> friendships = new HashSet<>();

    @Relationship(type = "FRIEND_REQUEST", direction = Relationship.Direction.OUTGOING)
    private Set<User> friendRequests = new HashSet<>();
}

