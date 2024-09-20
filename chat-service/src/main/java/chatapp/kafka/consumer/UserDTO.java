package chatapp.kafka.consumer;

import java.util.UUID;

public record UserDTO(
        UUID id ,
        String firstName,
        String lastName,
        String username,
        String email
) {

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

