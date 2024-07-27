package project2.gms.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.gms.jwt.JwtService;
import project2.gms.model.Membership;
import project2.gms.service.MembershipService;
import project2.gms.service.PaymentService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private MembershipService membershipService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getall")
    public ResponseEntity<?> membershipOptions(){
        List<Membership> memberships = membershipService.membershipOptions();
        return new ResponseEntity<>(memberships, HttpStatus.OK);
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renewMembership(@RequestHeader("Authorization") String token,
                                             @RequestParam String packageName){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username  == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Membership membership = membershipService.getMembershipByPackageName(packageName);
            Order order = paymentService.createOrder(membership.getPrice());
            return new ResponseEntity<>(order.toString(), HttpStatus.OK);
        }catch (RazorpayException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verifyPayment")
    public ResponseEntity<?> verifyPayment(@RequestHeader("Authorization") String token,
                                           @RequestParam String paymentId,
                                           @RequestParam String packageName){
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);

        if(username  == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            boolean isVerified = paymentService.verifyPayment(paymentId);
            if(isVerified) {
                String updateResponse = membershipService.updateMembership(username, packageName);
                return new ResponseEntity<>(updateResponse, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Payment verification failed", HttpStatus.BAD_REQUEST);
            }
        }catch(RazorpayException e){
                return  new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
