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
public class AdminAddRequest {

    private String username;
    private String password;
    private long phoneNo;
    private String email;
    private Role role;
}
