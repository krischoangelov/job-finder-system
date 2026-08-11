package app.service.user;

import app.exception.user.RecruiterAccessRequiredException;
import app.exception.user.UserNotFoundException;
import app.model.dto.user.UserDTO;
import app.model.dto.user.UserUpdateProfileRequest;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;

    @Test
    public void whenUpdateProfile_shouldUpdateUser_andReturnUpdatedUserDTO() {

        User user = User.builder()
                .id(userId)
                .username("testUser")
                .firstName("Test")
                .lastName("User")
                .profilePicture("default.png")
                .email("test@email.com")
                .password("encodedPassword")
                .role(UserRole.CANDIDATE)
                .createdOn(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserUpdateProfileRequest request = UserUpdateProfileRequest.builder()
                .username("updatedUser")
                .email("updated@email.com")
                .firstName("Updated")
                .lastName("Person")
                .build();

        UserDTO result = userService.updateProfile(userId, request);

        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@email.com", result.getEmail());
        assertEquals("Updated", result.getFirstName());
        assertEquals("Person", result.getLastName());

        verify(userRepository).save(user);
    }

    @Test
    public void whenUpdateProfileWithInvalidId_shouldThrowUserNotFoundException() {

        UUID invalidId = UUID.randomUUID();

        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        UserUpdateProfileRequest request = UserUpdateProfileRequest.builder()
                .username("updated")
                .email("updated@email.com")
                .firstName("Updated")
                .lastName("User")
                .build();

        assertThrows(UserNotFoundException.class, () -> userService.updateProfile(invalidId, request));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void whenRecruiterHasNoApplications_shouldReturnEmptyCandidateList() {

        User user = User.builder()
                .id(userId)
                .username("testUser")
                .firstName("Test")
                .lastName("User")
                .profilePicture("default.png")
                .email("test@email.com")
                .password("encodedPassword")
                .role(UserRole.CANDIDATE)
                .createdOn(LocalDateTime.now())
                .build();

        user.setRole(UserRole.RECRUITER);
        user.setCreatedJobs(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var result = userService.getAllCandidatesByRecruiter(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void whenGetById_shouldReturnUserDTO() {

        User user = User.builder()
                .id(userId)
                .username("testUser")
                .firstName("Test")
                .lastName("User")
                .profilePicture("default.png")
                .email("test@email.com")
                .password("encodedPassword")
                .role(UserRole.CANDIDATE)
                .createdOn(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("testUser", result.getUsername());
    }
}
