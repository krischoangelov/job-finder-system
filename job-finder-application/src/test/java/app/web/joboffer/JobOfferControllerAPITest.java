package app.web.joboffer;

import app.controller.joboffer.JobOfferController;
import app.controller.user.UserController;
import app.model.dto.joboffer.CreateJobOfferRequest;
import app.model.dto.joboffer.JobOfferDTO;
import app.model.dto.user.UserDTO;
import app.model.enums.EmploymentType;
import app.model.enums.UserRole;
import app.service.joboffer.JobOfferService;
import app.service.user.AuthenticationUserDetails;
import app.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.time.LocalDate;
import java.util.UUID;

import static app.util.JobOfferFactory.*;
import static app.util.UserFactory.createUserDTO;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(JobOfferController.class)
public class JobOfferControllerAPITest {

    @MockitoBean
    private JobOfferService jobOfferService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getJobDetails_shouldReturnJobDetailsModelAndView_andHttpStatus200() throws Exception {

        AuthenticationUserDetails principal = getCandidatePrincipal();

        UUID jobId = UUID.randomUUID();

        UserDTO candidate = createUserDTO();

        JobOfferDTO job = createJobOfferDTO();
        job.setId(jobId);

        when(userService.getById(principal.getId())).thenReturn(candidate);
        when(jobOfferService.getJobOfferById(jobId)).thenReturn(job);

        MockHttpServletRequestBuilder request = get("/jobs/{id}", jobId).with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("details-job"))
                .andExpect(model().attribute("job", job))
                .andExpect(model().attribute("isCandidate", true))
                .andExpect(model().attribute("isRecruiter", false));
    }

    @Test
    public void getCreateJobPage_shouldReturnCreateJobView_andHttpStatus200() throws Exception {

        AuthenticationUserDetails principal = getRecruiterPrincipal();

        MockHttpServletRequestBuilder request = get("/jobs/create").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("create-job"))
                .andExpect(model().attributeExists("jobOfferRequest"));
    }


    @Test
    public void editJobOffer_shouldRedirectToJobs_andHttpStatus302() throws Exception {

        AuthenticationUserDetails principal = getRecruiterPrincipal();

        UUID jobId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/jobs/{id}/edit", jobId)
                .with(user(principal))
                .with(csrf())
                .param("title", "Updated Java Developer")
                .param("company", "Updated Company")
                .param("location", "Berlin")
                .param("description", "Updated job description")
                .param("salary", "6000")
                .param("type", "FULL_TIME")
                .param("deadline", LocalDate.now().plusDays(20).toString());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/jobs"))
                .andExpect(redirectedUrl("/jobs"));
    }

    @Test
    public void deleteJobOffer_shouldRedirectToJobs_andHttpStatus302() throws Exception {

        AuthenticationUserDetails principal = getRecruiterPrincipal();

        UUID jobId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/jobs/{id}/delete", jobId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/jobs"))
                .andExpect(redirectedUrl("/jobs"));
    }
}
