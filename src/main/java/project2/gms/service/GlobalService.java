package project2.gms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project2.gms.dto.ResetPassword;
import project2.gms.model.Role;
import project2.gms.model.User;
import project2.gms.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GlobalService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getProfile(String username){
        return userRepository.findByUsername(username);
    }

    public String resetPassword(String username, ResetPassword newPassword){
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("Username not found");
        }else{
            User user = userOptional.get();

            user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            user.setPasswordResetRequired(false);
            userRepository.save(user);
        }

        return "your password has been reset successfully!";
    }

    public List<User> getAllTrainers() {
        return userRepository.findByRole(Role.TRAINER);
    }

}
