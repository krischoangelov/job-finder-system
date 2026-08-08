package com.jobfinder.interviewschedulingservice.model.dto;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InterviewResponse {

    private UUID id;

    private LocalDateTime scheduledAt;

    private String meetingLink;
    private String location;

    private InterviewType type;
    private InterviewStatus status;

    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
