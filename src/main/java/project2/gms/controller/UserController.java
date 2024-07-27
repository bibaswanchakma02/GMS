package project2.gms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.dto.RequestTrainer;
import project2.gms.dto.UserProfileEditRequest;
import project2.gms.jwt.JwtService;
import project2.gms.model.Membership;
import project2.gms.model.User;
import project2.gms.service.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;



    @GetMapping("/getallTrainers")
    public ResponseEntity<List<User>> getAllTrainers(){
        List<User> trainersList = userService.getTrainers();
        if(trainersList.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(trainersList, HttpStatus.OK);
        }
    }

    @PostMapping("/trainerRequest")
    public ResponseEntity<?> trainerRequest(@RequestBody RequestTrainer newRequest){
        try {
           String response =  userService.trainerRequest(newRequest);
           return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getmembershipdetails")
    public ResponseEntity<Membership> getMembershipDetails(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
           Membership userMembership = userService.getMembershipDetails(username);
           return new ResponseEntity<>(userMembership, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/editProfile")
    public ResponseEntity<?> editProfile(@RequestHeader("Authorization") String token, @RequestBody UserProfileEditRequest editRequest){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            User editResponse = userService.editProfile(username,editRequest);
            return new ResponseEntity<>(editResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
