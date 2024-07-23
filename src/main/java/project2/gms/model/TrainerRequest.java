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
@Document(collection = "trainer_requests")
public class TrainerRequest {


    @Id
    private UUID requestId;
    private String memberName;
    private String trainerName;
    private Date createdAt;
    private String status;


}
