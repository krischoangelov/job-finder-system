package com.jobfinder.interviewschedulingservice.service;

import com.jobfinder.interviewschedulingservice.exception.InterviewNotFoundException;
import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.repository.InterviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.jobfinder.interviewschedulingservice.factory.InterviewFactory.createRequest;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@SpringBootTest
public class InterviewServiceItTest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewService interviewService;

    @Test
    void whenGetInterviewById_shouldReturnCorrectInterview() {

        CreateInterviewRequest request = createRequest();

        InterviewResponse created = interviewService.createInterview(request);

        InterviewResponse found = interviewService.getInterviewById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getType(), found.getType());
    }

    @Test
    void whenGetInterviewByInvalidId_shouldThrowInterviewNotFoundException() {

        UUID id = UUID.randomUUID();

        assertThrows(InterviewNotFoundException.class, () -> interviewService.getInterviewById(id));
    }

    @Test
    void whenConfirmInterview_shouldChangeStatusToConfirmed() {

        InterviewResponse created = interviewService.createInterview(createRequest());
        InterviewResponse confirmed = interviewService.confirmInterview(created.getId());

        assertEquals(InterviewStatus.CONFIRMED, confirmed.getStatus());
        assertNotNull(confirmed.getUpdatedOn());
    }

    @Test
    void whenDeclineInterview_shouldChangeStatusToDeclined() {

        InterviewResponse created = interviewService.createInterview(createRequest());
        InterviewResponse declined = interviewService.declineInterview(created.getId());

        assertEquals(InterviewStatus.DECLINED, declined.getStatus());
    }

    @Test
    void whenCancelInterview_shouldChangeStatusToCancelled() {

        InterviewResponse created = interviewService.createInterview(createRequest());
        InterviewResponse cancelled = interviewService.cancelInterview(created.getId());

        assertEquals(InterviewStatus.CANCELLED, cancelled.getStatus());
    }


}
