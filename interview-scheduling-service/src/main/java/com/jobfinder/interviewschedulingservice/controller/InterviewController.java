package com.jobfinder.interviewschedulingservice.controller;

import com.jobfinder.interviewschedulingservice.model.dto.CreateInterviewRequest;
import com.jobfinder.interviewschedulingservice.model.dto.InterviewResponse;
import com.jobfinder.interviewschedulingservice.model.dto.UpdateInterviewRequest;
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

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<InterviewResponse>> getCandidateInterviews(
            @PathVariable UUID candidateId) {

        List<InterviewResponse> interviews =
                interviewService.getInterviewsByCandidateId(candidateId);

        return ResponseEntity.ok(interviews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInterviewRequest request) {

        InterviewResponse interview =
                interviewService.updateInterview(id, request);

        return ResponseEntity.ok(interview);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<InterviewResponse> confirmInterview(
            @PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.confirmInterview(id);

        return ResponseEntity.ok(interview);
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<InterviewResponse> declineInterview(
            @PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.declineInterview(id);

        return ResponseEntity.ok(interview);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<InterviewResponse> cancelInterview(
            @PathVariable UUID id) {

        InterviewResponse interview =
                interviewService.cancelInterview(id);

        return ResponseEntity.ok(interview);
    }


}
