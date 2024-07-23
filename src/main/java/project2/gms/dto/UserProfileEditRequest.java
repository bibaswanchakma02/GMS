package project2.gms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEditRequest {

    private String username;
    private long mobileNo;
    private String email;
    private String image;

}
