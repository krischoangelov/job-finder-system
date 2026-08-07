package com.jobfinder.interviewschedulingservice.model.entity;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID jobApplicationId;

    @Column(nullable = false)
    private UUID candidateId;

    @Column(nullable = false)
    private UUID recruiterId;

    @Column(nullable = false)
    private LocalDate interviewDate;

    @Column(nullable = false)
    private LocalTime interviewTime;

    private String meetingLink;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;
}
