package project2.gms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project2.gms.model.User;
import project2.gms.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GlobalService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getProfile(String username){
        return userRepository.findByUsername(username);
    }
}
