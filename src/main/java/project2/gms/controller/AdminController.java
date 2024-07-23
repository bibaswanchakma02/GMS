package project2.gms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.dto.AddMembership;
import project2.gms.dto.AddRequest;
import project2.gms.dto.AuthResponse;
import project2.gms.jwt.JwtService;
import project2.gms.model.Membership;
import project2.gms.model.User;
import project2.gms.service.AdminService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/profile")
    public ResponseEntity<Optional<User>> getAdminProfile(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Optional<User> user = adminService.getAdminProfile(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addmember")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AddRequest request){

        try {
            AuthResponse response = adminService.addUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getallmembers")
    public ResponseEntity<List<User>> getAllMembers(){
        List<User> members = adminService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/getalltrainers")
    public ResponseEntity<List<User>> getAllTrainers(){
        List<User> trainers = adminService.getAllTrainers();
        if(trainers.isEmpty()){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(trainers,HttpStatus.OK);
        }
    }

    @GetMapping("/getmember/{username}")
    public ResponseEntity<Optional<User>> getMember(@PathVariable("username") String username){
        Optional<User> user = adminService.getMember(username);
        if(user.isPresent()){
            return new ResponseEntity<>(user,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletemember/{username}")
    public ResponseEntity<String> deleteMember(@PathVariable("username") String username){
        boolean deleted = adminService.deleteMember(username);
        if (deleted){
            return ResponseEntity.ok("Successfully deleted");
        }else{
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @PostMapping("/addmembership")
    public ResponseEntity<String> addPackage(@RequestBody AddMembership addMembership){
       try {
           String newMembership = adminService.addMembership(addMembership);
           return new ResponseEntity<>(newMembership, HttpStatus.OK);
       }catch (Exception e){
           return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/editmembership")
    public ResponseEntity<Membership> editMembership(@RequestBody AddMembership editMembership){
        try {
            Membership membership = adminService.editMembership(editMembership);
            return new ResponseEntity<>(membership, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}