package app.util;

import app.model.dto.user.UserDTO;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.service.user.AuthenticationUserDetails;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class UserFactory {

    public static UserDTO createUserDTO() {
        return UserDTO.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .email("test@email.com")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.CANDIDATE)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static AuthenticationUserDetails getUserPrincipal() {
        return AuthenticationUserDetails.builder()
                .id(UUID.randomUUID())
                .username("User")
                .password("bleh")
                .role(UserRole.CANDIDATE)
                .build();
    }


    public static User createCandidate(String username, String email) {

        return User.builder()
                .username(username)
                .firstName("Test")
                .lastName("Candidate")
                .profilePicture("default.png")
                .email(email)
                .password("$2a$10$testEncodedPassword")
                .role(UserRole.CANDIDATE)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
