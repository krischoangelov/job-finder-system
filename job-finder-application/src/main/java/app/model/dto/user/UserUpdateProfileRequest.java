package app.model.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateProfileRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
