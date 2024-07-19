package project2.gms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project2.gms.model.User;
import project2.gms.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
    User findById(UUID id);
    Optional<List<User>> findByAssignedTrainer(String assignedTrainer);

    void deleteByUsername(String username);
}
