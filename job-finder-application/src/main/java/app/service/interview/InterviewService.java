package app.service.interview;

import app.model.dto.interview.CreateInterviewRequest;
import app.model.dto.interview.InterviewResponse;
import app.model.dto.interview.UpdateInterviewRequest;
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

    public List<InterviewResponse> getInterviewsByCandidateId(UUID candidateId) {
        return interviewClient.getCandidateInterviews(candidateId, APIKey);
    }

    public InterviewResponse updateInterview(
            UUID id,
            UpdateInterviewRequest request) {

        return interviewClient.updateInterview(id, request, APIKey);
    }

    public InterviewResponse confirmInterview(UUID id) {
        return interviewClient.confirmInterview(id, APIKey);
    }

    public InterviewResponse declineInterview(UUID id) {
        return interviewClient.declineInterview(id, APIKey);
    }

    public InterviewResponse cancelInterview(UUID id) {
        return interviewClient.cancelInterview(id, APIKey);
    }
}
