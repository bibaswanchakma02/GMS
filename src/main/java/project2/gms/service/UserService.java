package project2.gms.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project2.gms.dto.RequestTrainer;
import project2.gms.dto.ResetPassword;
import project2.gms.dto.UserProfileEditRequest;
import project2.gms.model.Membership;
import project2.gms.model.Role;
import project2.gms.model.TrainerRequest;
import project2.gms.model.User;
import project2.gms.repository.RequestRepository;
import project2.gms.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    public List<User> getTrainers(){
        return userRepository.findByRole(Role.TRAINER);
    }


    public String trainerRequest(RequestTrainer newRequest){
        Optional<User> trainer = userRepository.findByUsername(newRequest.getTrainerName());
        if(trainer.isEmpty()){
            return "trainer not found";
        }

        Optional<TrainerRequest> existingRequest = requestRepository.findByTrainerNameAndMemberName(newRequest.getTrainerName(), newRequest.getMemberName());
        if(existingRequest.isPresent()){
            return "Request already sent!";
        }

        Date date = new Date();
        var trainerRequest = TrainerRequest.builder()
                .requestId(UUID.randomUUID())
                .trainerName(trainer.get().getUsername())
                .memberName(newRequest.getMemberName())
                .createdAt(date)
                .status("PENDING")
                .build();

        requestRepository.save(trainerRequest);
        return "Request sent.";

    }

    public Membership getMembershipDetails(String username){
        return userRepository.findByUsername(username).get().getMembership();
    }


    public User editProfile(String username, UserProfileEditRequest editRequest){
        Optional<User> userToBeEdited = userRepository.findByUsername(username);

        if(userToBeEdited.isEmpty()){
            throw new UsernameNotFoundException("Username not found");
        }else{
            User user = userToBeEdited.get();

            user.setUsername(editRequest.getUsername());
            user.setMobileNo(editRequest.getMobileNo());
            user.setEmail(editRequest.getEmail());
            user.setImage(editRequest.getImage());

            return userRepository.save(user);

        }
    }


}
