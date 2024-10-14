package org.example.friendshipservice.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Relationship(type = "FRIENDSHIP", direction = Relationship.Direction.OUTGOING)
    private Set<Friendship> friendships = new HashSet<>();


    @Relationship(type = "FRIEND_REQUEST_Received", direction = Relationship.Direction.INCOMING)
    private Set<User> friendRequestsReceived = new HashSet<>();
    @JsonIgnore
    @Relationship(type = "FRIEND_REQUEST_Sent", direction = Relationship.Direction.OUTGOING)
    private Set<User> friendRequestsSent = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);  // Infinite loop if friendships are involved in hash calculation
    }
}

