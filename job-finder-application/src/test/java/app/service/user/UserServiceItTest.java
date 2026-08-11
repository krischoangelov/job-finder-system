package app.service.user;

import app.exception.user.PasswordMismatchException;
import app.model.dto.user.UserDTO;
import app.model.dto.user.UserRegisterRequestDTO;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.repository.jobapplication.JobApplicationRepository;
import app.repository.joboffer.JobOfferRepository;
import app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static app.util.UserFactory.createCandidate;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
public class UserServiceItTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Test
    public void whenRegister_shouldPersistUser_andEncodePassword() {

        UserRegisterRequestDTO request = UserRegisterRequestDTO.builder()
                .username("newUser")
                .firstName("New")
                .lastName("User")
                .email("newuser@abv.bg")
                .password("password123")
                .confirmPassword("password123")
                .role(UserRole.CANDIDATE)
                .build();

        UserDTO result = userService.register(request);

        assertNotNull(result);
        assertNotNull(result.getId());

        User savedUser = userRepository.findByUsername("newUser").orElseThrow();

        assertEquals("newuser@abv.bg", savedUser.getEmail());
        assertNotEquals("password123", savedUser.getPassword());
    }

    @Test
    public void whenRegisterWithDifferentPasswords_shouldThrowPasswordMismatchException() {

        UserRegisterRequestDTO request =
                UserRegisterRequestDTO.builder()
                        .username("newUser")
                        .firstName("New")
                        .lastName("User")
                        .email("new@test.com")
                        .password("password123")
                        .confirmPassword("wrongPassword")
                        .role(UserRole.CANDIDATE)
                        .build();

        assertThrows(PasswordMismatchException.class, () -> userService.register(request)
        );
    }


    @Test
    public void whenLoadUserByUsername_shouldReturnUserDetails() {

        User user = userRepository.save(createCandidate("loginUser", "loggnat@abv.bg"));

        UserDetails result = userService.loadUserByUsername("loginUser");

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }
}
