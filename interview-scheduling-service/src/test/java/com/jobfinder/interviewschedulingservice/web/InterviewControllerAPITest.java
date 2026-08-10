package com.jobfinder.interviewschedulingservice.web;

import com.jobfinder.interviewschedulingservice.controller.InterviewController;
import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.dto.UpdateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import com.jobfinder.interviewschedulingservice.service.InterviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.jobfinder.interviewschedulingservice.factory.InterviewFactory.createResponse;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.http.HttpHeaders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.jobfinder.interviewschedulingservice.factory.InterviewFactory.getInterviewResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(InterviewController.class)
public class InterviewControllerAPITest {

    @MockitoBean
    private InterviewService interviewService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-Key", "api-key");
        return headers;
    }

    @Test
    public void whenGetInterviewById_shouldReturnInterview_andReturnStatus200() throws Exception {

        InterviewResponse interviewResponse = getInterviewResponse();

        when(interviewService.getInterviewById(any())).thenReturn(interviewResponse);

        MockHttpServletRequestBuilder request =
                get("/api/interviews/{id}", interviewResponse.getId())
                        .headers(getHeaders());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(interviewResponse.getId().toString()))
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.type").value("ONLINE"))
                .andExpect(jsonPath("$.location").value("Sofia"));
    }

    @Test
    public void whenCreateInterview_shouldReturnCreatedInterview_andReturnStatus201() throws Exception {

        CreateInterviewRequest createRequest = CreateInterviewRequest.builder()
                .jobApplicationId(UUID.randomUUID())
                .candidateId(UUID.randomUUID())
                .recruiterId(UUID.randomUUID())
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .meetingLink("https://jobs-interview.bg/interview")
                .location("Sofia")
                .type(InterviewType.ONLINE)
                .build();

        InterviewResponse interviewResponse = getInterviewResponse();

        when(interviewService.createInterview(any(CreateInterviewRequest.class)))
                .thenReturn(interviewResponse);

        MockHttpServletRequestBuilder request =
                post("/api/interviews")
                        .headers(getHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(interviewResponse.getId().toString()))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }


    @Test
    public void whenUpdateInterview_shouldReturnUpdatedInterview_andReturnStatus200() throws Exception {

        UUID interviewId = UUID.randomUUID();

        UpdateInterviewRequest updateRequest = UpdateInterviewRequest.builder()
                .scheduledAt(LocalDateTime.now().plusDays(5))
                .meetingLink("https://jobs-interview.bg/interview")
                .location("Plovdiv")
                .type(InterviewType.ONLINE)
                .build();

        InterviewResponse interviewResponse = InterviewResponse.builder()
                .id(interviewId)
                .scheduledAt(updateRequest.getScheduledAt())
                .meetingLink(updateRequest.getMeetingLink())
                .location(updateRequest.getLocation())
                .type(updateRequest.getType())
                .status(InterviewStatus.SCHEDULED)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(interviewService.updateInterview(eq(interviewId), any(UpdateInterviewRequest.class)))
                .thenReturn(interviewResponse);

        MockHttpServletRequestBuilder request =
                put("/api/interviews/{id}", interviewId)
                        .headers(getHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(interviewId.toString()))
                .andExpect(jsonPath("$.location").value("Plovdiv"))
                .andExpect(jsonPath("$.meetingLink").value("https://jobs-interview.bg/interview"));
    }

    @Test
    void updateInterview_withPastDate_shouldReturnBadRequest()
            throws Exception {

        UUID id = UUID.randomUUID();

        UpdateInterviewRequest request = UpdateInterviewRequest.builder()
                        .scheduledAt(LocalDateTime.now().minusDays(1))
                        .type(InterviewType.ONLINE)
                        .build();

        mockMvc.perform(put("/api/interviews/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenConfirmInterview_shouldReturnConfirmedInterview_andReturnStatus200() throws Exception {

        InterviewResponse interviewResponse = getInterviewResponse();
        interviewResponse.setStatus(InterviewStatus.CONFIRMED);

        when(interviewService.confirmInterview(any())).thenReturn(interviewResponse);

        MockHttpServletRequestBuilder request =
                put("/api/interviews/{id}/confirm", interviewResponse.getId())
                        .headers(getHeaders());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void cancelInterview_shouldReturnCancelledInterview() throws Exception {

        UUID id = UUID.randomUUID();

        InterviewResponse response = createResponse(id);
        response.setStatus(InterviewStatus.CANCELLED);

        when(interviewService.cancelInterview(id))
                .thenReturn(response);

        MockHttpServletRequestBuilder mockHttpServletRequest = put("/api/interviews/{id}/cancel", id)
                .headers(getHeaders());

        mockMvc.perform(mockHttpServletRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    public void whenInvalidAPIKey_shouldReturnUnauthorized() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-API-Key", "invalid-api-key");

        MockHttpServletRequestBuilder request =
                get("/api/interviews/recruiter/{recruiterId}", UUID.randomUUID())
                        .headers(httpHeaders);

        mockMvc.perform(request).andExpect(status().isForbidden());
    }
}
