package project2.gms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project2.gms.model.User;
import project2.gms.repository.UserRepository;

import java.util.List;

@Component
public class MembershipStatusUpdater {

    @Autowired
    private UserRepository userRepository;


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateMembershipStatuses(){
         List<User> users = userRepository.findAll();

         for(User user : users){
            user.updateMembershipStatus();
            userRepository.save(user);
         }
    }
}
