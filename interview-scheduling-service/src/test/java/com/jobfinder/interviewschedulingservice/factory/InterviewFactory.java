package com.jobfinder.interviewschedulingservice.factory;

import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class InterviewFactory {

    public static InterviewResponse getInterviewResponse() {
        return InterviewResponse.builder()
                .id(UUID.randomUUID())
                .scheduledAt(LocalDateTime.now().plusDays(2))
                .meetingLink("https://meet.example.com/interview")
                .location("Sofia")
                .type(InterviewType.ONLINE)
                .status(InterviewStatus.SCHEDULED)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static CreateInterviewRequest createRequest() {
        return CreateInterviewRequest.builder()
                .jobApplicationId(UUID.randomUUID())
                .candidateId(UUID.randomUUID())
                .recruiterId(UUID.randomUUID())
                .scheduledAt(LocalDateTime.now().plusDays(3))
                .meetingLink("https://meet.example.com/interview")
                .location("Sofia")
                .type(InterviewType.ONLINE)
                .build();
    }

    public static InterviewResponse createResponse(UUID id) {
        return InterviewResponse.builder()
                .id(id)
                .scheduledAt(LocalDateTime.now().plusDays(2))
                .meetingLink("https://meet.example.com/interview")
                .location("Sofia")
                .type(InterviewType.ONLINE)
                .status(InterviewStatus.SCHEDULED)
                .createdOn(LocalDateTime.now())
                .build();
    }
}
