package app.service.interview.client;

import app.model.dto.interview.CreateInterviewRequest;
import app.model.dto.interview.InterviewResponse;
import app.model.dto.interview.UpdateInterviewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@FeignClient(
        name = "interview-scheduling-service",
        url = "${interview-svc-base-url}"
)
public interface InterviewClient {

    String X_API_KEY = "X-API-Key";

    @PostMapping()
    InterviewResponse createInterview(
            @RequestBody CreateInterviewRequest request,
            @RequestHeader(X_API_KEY) String xAPIKey

    );

    @GetMapping("/{id}")
    InterviewResponse getInterview(
            @PathVariable UUID id,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @GetMapping("/recruiter/{recruiterId}")
    List<InterviewResponse> getRecruiterInterviews(
            @PathVariable UUID recruiterId,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @GetMapping("/api/interviews/candidate/{candidateId}")
    List<InterviewResponse> getCandidateInterviews(
            @PathVariable UUID candidateId,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @PutMapping("/api/interviews/{id}")
    InterviewResponse updateInterview(
            @PathVariable UUID id,
            @RequestBody UpdateInterviewRequest request,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @PutMapping("/api/interviews/{id}/confirm")
    InterviewResponse confirmInterview(
            @PathVariable UUID id,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @PutMapping("/api/interviews/{id}/decline")
    InterviewResponse declineInterview(
            @PathVariable UUID id,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @PutMapping("/api/interviews/{id}/cancel")
    InterviewResponse cancelInterview(
            @PathVariable UUID id,
            @RequestHeader(X_API_KEY) String xAPIKey
    );
}
