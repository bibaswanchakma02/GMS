package project2.gms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.jwt.JwtService;
import project2.gms.model.TrainerRequest;
import project2.gms.model.User;
import project2.gms.service.TrainerService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/trainer")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;
    @Autowired
    private JwtService jwtService;



    @GetMapping("/getmembers")
    public ResponseEntity<Optional<List<User>>> getMembers(@RequestParam String assignedTrainer){
        Optional<List<User>> members = trainerService.getMembers(assignedTrainer);
        if(members.isPresent()){
            return new ResponseEntity<>(members, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getRequests")
    public ResponseEntity<List<TrainerRequest>> getPendingRequests(@RequestHeader("Authorization") String token){

        String jwtToken = token.substring(7);
        String trainerName = jwtService.extractUsername(jwtToken);

        if(trainerName == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        List<TrainerRequest> requests = trainerService.getRequests(trainerName);

        if(requests.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(requests, HttpStatus.OK);

    }




}
