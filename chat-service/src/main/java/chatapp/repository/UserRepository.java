package chatapp.repository;

import chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userId <> ?1")
    List<User> findAllUsersExceptThisUserId(UUID userId);
}
