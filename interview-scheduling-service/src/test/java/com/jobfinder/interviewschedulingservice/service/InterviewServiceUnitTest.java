package com.jobfinder.interviewschedulingservice.service;


import com.jobfinder.interviewschedulingservice.exception.GlobalExceptionHandler;
import com.jobfinder.interviewschedulingservice.exception.InterviewNotFoundException;
import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static com.jobfinder.interviewschedulingservice.factory.InterviewFactory.createRequest;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class InterviewServiceUnitTest {

    @Autowired
    private InterviewService interviewService;

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Test
    public void whenGetInterviewsByCandidateId_shouldReturnOnlyCandidateInterviews() {

        UUID candidateId = UUID.randomUUID();

        CreateInterviewRequest firstRequest = createRequest();
        firstRequest.setCandidateId(candidateId);

        CreateInterviewRequest secondRequest = createRequest();
        secondRequest.setCandidateId(candidateId);

        CreateInterviewRequest thirdRequest = createRequest();
        thirdRequest.setCandidateId(UUID.randomUUID());

        interviewService.createInterview(firstRequest);
        interviewService.createInterview(secondRequest);
        interviewService.createInterview(thirdRequest);

        List<InterviewResponse> interviews = interviewService.getInterviewsByCandidateId(candidateId);

        assertEquals(2, interviews.size());
    }

    @Test
    public void whenCancelInterview_shouldChangeStatusToCancelled() {

        InterviewResponse createdInterview = interviewService.createInterview(createRequest());
        InterviewResponse cancelledInterview = interviewService.cancelInterview(createdInterview.getId());

        assertEquals(InterviewStatus.CANCELLED, cancelledInterview.getStatus());

        assertNotNull(cancelledInterview.getUpdatedOn());

        InterviewResponse interviewFromDatabase = interviewService.getInterviewById(createdInterview.getId());

        assertEquals(InterviewStatus.CANCELLED, interviewFromDatabase.getStatus());
    }

    @Test
    public void whenCancelInterviewWithInvalidId_shouldThrowInterviewNotFoundException() {

        UUID invalidId = UUID.randomUUID();

        assertThrows(InterviewNotFoundException.class, () -> interviewService.cancelInterview(invalidId));
    }

}
