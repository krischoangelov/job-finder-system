package com.jobfinder.interviewschedulingservice.model.dto;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInterviewRequest {

    private UUID jobApplicationId;

    private UUID candidateId;

    private UUID recruiterId;

    private LocalDateTime scheduledAt;

    private String meetingLink;

    private String location;

    private InterviewType type;
}
