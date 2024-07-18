package project2.gms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project2.gms.dto.AddMembership;
import project2.gms.dto.AddRequest;
import project2.gms.dto.AuthResponse;
import project2.gms.model.Membership;
import project2.gms.model.User;
import project2.gms.model.Role;
import project2.gms.repository.MembershipRepository;
import project2.gms.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private MembershipRepository membershipRepository;


    public Optional<User> getAdminProfile(String username){
        return userRepository.findByUsername(username);
    }


    public AuthResponse addUser(AddRequest request){
        Optional<User> member = userRepository.findByUsername(request.getUsername());
        if(member.isPresent()){
            return AuthResponse.builder().responseString("Username already exists!").build();
        }

        Role role = (request.getRole() != null && !request.getRole().describeConstable().isEmpty()) ? request.getRole() : Role.USER;


        var user = User.builder()
                .id(UUID.randomUUID())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNo(request.getMobileNo())
                .email(request.getEmail())
                .image(request.getImage())
                .role(role)
                .build();

        userRepository.save(user);
        return AuthResponse.builder().responseString("User successfully registered!").build();

    }

    public List<User> getAllMembers() {
        return userRepository.findAll();
    }

    public List<User> getAllTrainers() {
        return userRepository.findByRole(Role.TRAINER);
    }

    public Optional<User> getMember(String username){
        return userRepository.findByUsername(username);

    }

    public boolean deleteMember(String username){
        if(userRepository.findByUsername(username).isPresent()){
            userRepository.deleteByUsername(username);
            return true;
        }else{
            return false;
        }
    }

    public String addMembership(AddMembership addMembership){
        Optional<Membership> membership = membershipRepository.findByPackageName(addMembership.getPackageName());
        if(membership.isPresent()){
            return "Membership package already exists!";
        }

        Date date = new Date();

        Membership.Benefits benefits = Membership.Benefits.builder()
                .accessToAllFacilities(addMembership.getBenefits().isAccessToAllFacilities())
                .freePersonalTrainerSessions(addMembership.getBenefits().getFreePersonalTrainerSessions())
                .freeGroupClasses(addMembership.getBenefits().isFreeGroupClasses())
                .nutritionPlan(addMembership.getBenefits().isNutritionPlan())
                .guestPasses(addMembership.getBenefits().getGuestPasses())
                .merchandiseDiscount(addMembership.getBenefits().getMerchandiseDiscount())
                .lockerServices(addMembership.getBenefits().isLockerServices())
                .parking(addMembership.getBenefits().isParking())
                .other(addMembership.getBenefits().getOther())
                .build();


        Membership newMembership = Membership.builder()
                .packageId(UUID.randomUUID())
                .packageName(addMembership.getPackageName())
                .price(addMembership.getPrice())
                .benefits(benefits)
                .duration(addMembership.getDuration())
                .createdDate(date)
                .build();

        membershipRepository.save(newMembership);
        return "New membership added";
    }

}
