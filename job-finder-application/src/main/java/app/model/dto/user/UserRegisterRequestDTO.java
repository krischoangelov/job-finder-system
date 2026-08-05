package app.model.dto.user;

import app.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserRegisterRequestDTO {
    @Size(min = 6, message = "Username must be at least 8 characters")
    private String username;
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String password;
    @Size(min = 8, message = "Username must be at least 8 characters")
    private String confirmPassword;
    @NotNull(message = "Role is required")
    private UserRole role;
}
