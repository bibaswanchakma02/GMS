package project2.gms.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "membership")
public class Membership {

    @Id
    private UUID packageId;
    private String packageName;
    private double price;
    private boolean paymentStatus;
    private boolean renewalStatus;
    private Benefits benefits;
    private int duration; //in days
    private Date membershipStartDate;
    private Date membershipExpiryDate;
    private String status;
    private String paymentMethod;
    private Date createdDate;
    private String notes;



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Benefits{
        private boolean accessToAllFacilities;
        private int freePersonalTrainerSessions;
        private boolean freeGroupClasses;
        private boolean nutritionPlan;
        private int guestPasses;
        private double merchandiseDiscount;
        private boolean lockerServices;
        private boolean parking;
        private String other;
    }


}
