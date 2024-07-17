package project2.gms.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project2.gms.model.Role;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRequest {

    private String username;
    private String password;
    private long mobileNo;
    private String email;
    private String image;
    private Role role;
}
