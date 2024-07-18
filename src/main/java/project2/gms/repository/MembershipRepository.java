package project2.gms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project2.gms.model.Membership;

import java.util.Optional;
import java.util.UUID;


public interface MembershipRepository extends MongoRepository<Membership, UUID> {
    Optional<Membership> findById(UUID packageId);
    Optional<Membership> findByPackageName(String packageName);
}
