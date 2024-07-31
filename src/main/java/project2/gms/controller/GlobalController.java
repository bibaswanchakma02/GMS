package project2.gms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.dto.ResetPassword;
import project2.gms.jwt.JwtService;
import project2.gms.model.User;
import project2.gms.service.GlobalService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class GlobalController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private GlobalService globalService;

    @GetMapping("/profile")
    public ResponseEntity<Optional<User>> getAdminProfile(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = globalService.getProfile(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String token, @RequestBody ResetPassword newPassword){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        try {
            String response = globalService.resetPassword(username, newPassword);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getalltrainers")
    public ResponseEntity<List<User>> getAllTrainers(){
        List<User> trainers = globalService.getAllTrainers();
        if(trainers.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(trainers,HttpStatus.OK);
        }
    }
}
