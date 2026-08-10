package com.jobfinder.interviewschedulingservice.model.entity;

import com.jobfinder.interviewschedulingservice.model.enums.InterviewStatus;
import com.jobfinder.interviewschedulingservice.model.enums.InterviewType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID jobApplicationId;

    @NotNull
    @Column(nullable = false)
    private UUID candidateId;

    @NotNull
    @Column(nullable = false)
    private UUID recruiterId;

    @NotNull
    @Column(nullable = false)
    private LocalDate interviewDate;

    @NotNull
    @Column(nullable = false)
    private LocalTime interviewTime;

    @Size(max = 200)
    @Column(length = 200)
    private String meetingLink;

    @Size(max = 100)
    @Column(length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;
}
