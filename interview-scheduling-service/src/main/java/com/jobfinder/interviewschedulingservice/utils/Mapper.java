package com.jobfinder.interviewschedulingservice.utils;

import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.entity.Interview;

import java.time.LocalDateTime;

public class Mapper {

    public static InterviewResponse mapToInterviewResponse(Interview interview) {

        LocalDateTime scheduledAt = LocalDateTime.of(
                interview.getInterviewDate(),
                interview.getInterviewTime()
        );

        return InterviewResponse.builder()
                .id(interview.getId())
                .scheduledAt(scheduledAt)
                .meetingLink(interview.getMeetingLink())
                .location(interview.getLocation())
                .type(interview.getType())
                .status(interview.getStatus())
                .createdOn(interview.getCreatedOn())
                .updatedOn(interview.getUpdatedOn())
                .build();
    }
}
