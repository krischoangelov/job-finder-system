package app.web.user;

import app.controller.user.UserController;
import app.model.dto.user.UserDTO;
import app.model.enums.UserRole;
import app.service.user.AuthenticationUserDetails;
import app.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static app.util.UserFactory.createUserDTO;
import static app.util.UserFactory.getUserPrincipal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerAPITest {

    @MockitoBean
    private UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getProfile_shouldReturnProfileModelAndView_andHttpStatus200() throws Exception {
        // Given
        UserDTO userDTO = createUserDTO();
        when(userService.getById(any())).thenReturn(userDTO);

        MockHttpServletRequestBuilder request = get("/profile", userDTO.getId())
                .with(user(getUserPrincipal()));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profile"))
                .andExpect(MockMvcResultMatchers.model().attribute("userDTO", userDTO));
    }

    @Test
    public void getDashboard_shouldReturnDashboardModelAndView_andHttpStatus200() throws Exception {

        AuthenticationUserDetails principal = getUserPrincipal();
        UserDTO userDTO = createUserDTO();
        userDTO.setCreatedJobs(List.of());
        userDTO.setJobApplications(List.of());
        userDTO.setSkills(List.of());

        when(userService.getById(principal.getId())).thenReturn(userDTO);

        MockHttpServletRequestBuilder request = get("/dashboard")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("dashboard"))
                .andExpect(MockMvcResultMatchers.model().attribute("user", userDTO));
    }

    @Test
    public void getCandidates_shouldReturnCandidatesModelAndView_andHttpStatus200() throws Exception {

        AuthenticationUserDetails principal = getUserPrincipal();
        principal.setRole(UserRole.RECRUITER);

        UserDTO recruiterDTO = createUserDTO();
        recruiterDTO.setRole(UserRole.RECRUITER);

        List<UserDTO> candidates = List.of(
                UserDTO.builder()
                        .id(UUID.randomUUID())
                        .username("candidate1")
                        .role(UserRole.CANDIDATE)
                        .build(),
                UserDTO.builder()
                        .id(UUID.randomUUID())
                        .username("candidate2")
                        .role(UserRole.CANDIDATE)
                        .build()
        );

        when(userService.getById(principal.getId())).thenReturn(recruiterDTO);
        when(userService.getAllCandidatesByRecruiter(principal.getId())).thenReturn(candidates);

        MockHttpServletRequestBuilder request = get("/candidates")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("candidates"))
                .andExpect(MockMvcResultMatchers.model().attribute("candidates", candidates));
    }
}
