package com.jobfinder.interviewschedulingservice.model.dto;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInterviewRequest {

    private LocalDateTime scheduledAt;

    private String meetingLink;

    private String location;

    private InterviewType type;
}
