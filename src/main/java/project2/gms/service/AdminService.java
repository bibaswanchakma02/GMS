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

import java.util.*;

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


    public AuthResponse addUser(AddRequest request) {
        Optional<User> member = userRepository.findByUsername(request.getUsername());
        if (member.isPresent()) {
            return AuthResponse.builder().responseString("Username already exists!").build();
        }


        Role role = (request.getRole() != null && !request.getRole().describeConstable().isEmpty()) ?
                request.getRole() : Role.USER;

        Membership userMembership = null;
        if (request.getPackageName() != null && !request.getPackageName().isEmpty()) {
            Optional<Membership> optionalMembership = membershipRepository.findByPackageName(request.getPackageName());

            if(optionalMembership.isPresent()){
                Membership existingMembership = optionalMembership.get();

                Date membershipStartdate = new Date();
                int duration = existingMembership.getDuration();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(membershipStartdate);
                calendar.add(Calendar.DAY_OF_YEAR, duration);
                Date membershipExpiryDate = calendar.getTime();


                 userMembership = Membership.builder()
                        .packageId(existingMembership.getPackageId())
                        .packageName(existingMembership.getPackageName())
                        .price(existingMembership.getPrice())
                        .paymentStatus(true)
                        .renewalStatus(false)
                        .benefits(existingMembership.getBenefits())
                        .duration(existingMembership.getDuration())
                        .membershipStartDate(membershipStartdate)
                        .membershipExpiryDate(membershipExpiryDate)
                        .status("Active")
                        .paymentMethod("Card")
                        .notes("First membership")
                        .build();
            }

        }



        var user = User.builder()
                .id(UUID.randomUUID())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNo(request.getMobileNo())
                .email(request.getEmail())
                .image(request.getImage())
                .role(role)
                .assignedTrainer(request.getAssignedTrainer())
                .membership(userMembership)
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

    public Membership editMembership(AddMembership editMembership) {
        Optional<Membership> existingMembership = membershipRepository.findByPackageName(editMembership.getPackageName());
        if(existingMembership.isEmpty()){
            throw new IllegalArgumentException("Membership not found!");
        }

        Membership membership = existingMembership.get();

        membership.setPackageName(editMembership.getPackageName());
        membership.setPrice(editMembership.getPrice());
        membership.setDuration(editMembership.getDuration());
        membership.setNotes(editMembership.getNotes());

        if(editMembership.getBenefits()!= null){
            Membership.Benefits benefits = Membership.Benefits.builder()
                    .accessToAllFacilities(editMembership.getBenefits().isAccessToAllFacilities())
                    .freePersonalTrainerSessions(editMembership.getBenefits().getFreePersonalTrainerSessions())
                    .freeGroupClasses(editMembership.getBenefits().isFreeGroupClasses())
                    .nutritionPlan(editMembership.getBenefits().isNutritionPlan())
                    .guestPasses(editMembership.getBenefits().getGuestPasses())
                    .merchandiseDiscount(editMembership.getBenefits().getMerchandiseDiscount())
                    .lockerServices(editMembership.getBenefits().isLockerServices())
                    .parking(editMembership.getBenefits().isParking())
                    .other(editMembership.getBenefits().getOther())
                    .build();

            membership.setBenefits(benefits);
        }

        return membershipRepository.save(membership);

    }

}
