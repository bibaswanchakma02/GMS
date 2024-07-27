package project2.gms.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.jwt.JwtService;
import project2.gms.model.User;
import project2.gms.service.GlobalService;

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
}
