package app.service.interview.client;

import app.model.dto.interview.CreateInterviewRequest;
import app.model.dto.interview.InterviewResponse;
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

    @PostMapping("/api/interviews")
    InterviewResponse createInterview(
            @RequestBody CreateInterviewRequest request,
            @RequestHeader(X_API_KEY) String xAPIKey

    );

    @GetMapping("/api/interviews/{id}")
    InterviewResponse getInterview(
            @PathVariable UUID id,
            @RequestHeader(X_API_KEY) String xAPIKey
    );

    @GetMapping("/api/interviews/recruiter/{recruiterId}")
    List<InterviewResponse> getRecruiterInterviews(
            @PathVariable UUID recruiterId,
            @RequestHeader(X_API_KEY) String xAPIKey
    );
}
