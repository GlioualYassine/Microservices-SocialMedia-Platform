package org.example.friendshipservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class Friendship {

    @Id
    @GeneratedValue
    private Long  id; // Unique identifier for the relationship

    @TargetNode
    private User friend; // The friend user this relationship points to

    private LocalDateTime since; // Date when the friendship was established

    private String status; // Status of the friendship (e.g., "ACTIVE", "PENDING", "BLOCKED")
}
