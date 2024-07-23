package project2.gms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project2.gms.model.Membership;
import project2.gms.model.User;
import project2.gms.repository.MembershipRepository;
import project2.gms.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public List<Membership> membershipOptions(){
        return membershipRepository.findAll();
    }

    public Membership getMembershipByPackageName(String packageName){
        return membershipRepository.findByPackageName(packageName).orElseThrow(()->new RuntimeException("Membership not found"));
    }

    public String updateMembership (String username, String packageName){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            Membership membership = membershipRepository.findByPackageName(packageName).orElseThrow(()->new RuntimeException("Membership not found"));

            Date date =  new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, membership.getDuration());
            Date expiryDate = calendar.getTime();

            membership.setMembershipStartDate(date);
            membership.setMembershipExpiryDate(expiryDate);
            membership.setPaymentStatus(true);
            membership.setRenewalStatus(true);
            membership.setStatus("active");

            user.setMembership(membership);

            userRepository.save(user);

            return "Membership renewed";
        }else {
            throw new UsernameNotFoundException("username not found");
        }
    }
}
