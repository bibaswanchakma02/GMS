package project2.gms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project2.gms.model.TrainerRequest;
import project2.gms.model.User;
import project2.gms.repository.RequestRepository;
import project2.gms.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    public Optional<User> getProfile(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<List<User>> getMembers(String assignedTrainer){
        return userRepository.findByAssignedTrainer(assignedTrainer);
    }

    public List<TrainerRequest> getRequests(String trainerName){
        List<TrainerRequest> pendingRequests = requestRepository.findByTrainerNameAndStatus(trainerName, "PENDING");
        return  pendingRequests;

    }
}
