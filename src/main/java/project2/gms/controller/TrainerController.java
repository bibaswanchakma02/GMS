package project2.gms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.model.User;
import project2.gms.service.TrainerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/trainer")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping("/profile/{username}")
    public ResponseEntity<Optional<User>> getProfile(@PathVariable String username){
        Optional<User> profile = trainerService.getProfile(username);
        if(profile.isPresent()){
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/getmembers")
    public ResponseEntity<Optional<List<User>>> getMembers(@RequestParam String assignedTrainer){
        Optional<List<User>> members = trainerService.getMembers(assignedTrainer);
        if(members.isPresent()){
            return new ResponseEntity<>(members, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }



}
