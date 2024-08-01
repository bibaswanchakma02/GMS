package project2.gms.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project2.gms.model.Membership;
import project2.gms.model.User;
import project2.gms.repository.MembershipRepository;
import project2.gms.repository.UserRepository;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public List<Membership> membershipOptions(){
        return membershipRepository.findAll();
    }

    public Membership getMembershipByPackageName(String packageName){
        return membershipRepository.findByPackageName(packageName).orElseThrow(()->new RuntimeException("Membership not found"));
    }

    public String updateMembership (String username, String packageName){
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<Membership> newMembershipOptional = membershipRepository.findByPackageName(packageName);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Username not found");
        }

        User user = userOptional.get();
        Membership oldMembership = user.getMembership();
        Membership newMembership = newMembershipOptional.get();
        Date currentDate =  new Date();

        if(oldMembership.getMembershipExpiryDate().before(currentDate)){

            updateUserMembership(user, newMembership);
            return "membership renewed successfully";
        }else{
            long delay = oldMembership.getMembershipExpiryDate().getTime() - currentDate.getTime();
            scheduler.schedule(()->updateUserMembership(user, newMembership), delay, TimeUnit.MILLISECONDS);
            return "Membership renewed";
        }
    }

    private void updateUserMembership(User user, Membership newMembership){
        Date currentDate = new Date();
        Date startDate = currentDate.after(user.getMembership().getMembershipExpiryDate()) ?
                currentDate : user.getMembership().getMembershipExpiryDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE, newMembership.getDuration());
        Date expiryDate = calendar.getTime();

        newMembership.setMembershipStartDate(startDate);
        newMembership.setMembershipExpiryDate(expiryDate);
        newMembership.setPaymentStatus(true);
        newMembership.setRenewalStatus(true);
        newMembership.setStatus("active");

        user.setMembership(newMembership);

        userRepository.save(user);

        System.out.println("Membership renewed for user: " + user.getUsername());

    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.MINUTES)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
