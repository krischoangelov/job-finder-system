package app.service.interview;

import app.model.dto.interview.CreateInterviewRequest;
import app.model.dto.interview.InterviewResponse;
import app.service.interview.client.InterviewClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InterviewService {
    private final InterviewClient interviewClient;

    @Value("${interview.service.api-key}")
    private String APIKey;

    public InterviewService(InterviewClient interviewClient) {
        this.interviewClient = interviewClient;
    }

    public InterviewResponse createInterview(CreateInterviewRequest request) {
        return interviewClient.createInterview(request, APIKey);
    }

    public InterviewResponse getInterviewById(UUID id) {
        return interviewClient.getInterview(id, APIKey);
    }

    public List<InterviewResponse> getInterviewsByRecruiterId(UUID recruiterId) {

        return interviewClient.getRecruiterInterviews(recruiterId, APIKey);
    }
}
