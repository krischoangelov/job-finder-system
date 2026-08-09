package com.jobfinder.interviewschedulingservice.model.dto;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UpdateInterviewRequest {

    private LocalDateTime scheduledAt;

    private String meetingLink;

    private String location;

    private InterviewType type;
}
