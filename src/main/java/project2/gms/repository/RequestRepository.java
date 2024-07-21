package project2.gms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project2.gms.model.TrainerRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends MongoRepository<TrainerRequest, UUID> {

    List<TrainerRequest> findByTrainerNameAndStatus(String trainerName, String status);
    Optional<TrainerRequest> findByTrainerNameAndMemberName(String trainerName, String memberName);
}
