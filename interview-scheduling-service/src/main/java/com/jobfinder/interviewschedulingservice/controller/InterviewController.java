package com.jobfinder.interviewschedulingservice.controller;

import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> createInterview(
            @Valid @RequestBody CreateInterviewRequest request) {

        InterviewResponse interview =
                interviewService.createInterview(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(interview);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterview(
            @PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.getInterviewById(id);

        return ResponseEntity.ok(interview);
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<InterviewResponse>> getRecruiterInterviews(
            @PathVariable UUID recruiterId) {

        return ResponseEntity.ok(
                interviewService.getInterviewsByRecruiterId(recruiterId)
        );
    }


}
