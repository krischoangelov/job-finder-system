package app.service.jobapplication;

import app.model.dto.jobapplication.CreateJobApplicationRequest;
import app.model.dto.jobapplication.JobApplicationDTO;
import app.model.entity.jobapplication.JobApplication;
import app.model.entity.joboffer.JobOffer;
import app.model.entity.user.User;
import app.model.enums.ApplicationStatus;
import app.model.enums.UserRole;
import app.repository.jobapplication.JobApplicationRepository;
import app.repository.joboffer.JobOfferRepository;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobApplicationService {

    private JobApplicationRepository jobApplicationRepository;
    private UserRepository userRepository;
    private JobOfferRepository jobOfferRepository;

    @Autowired
    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository
            , JobOfferRepository jobOfferRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    public List<JobApplicationDTO> getApplicationsForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole().equals(UserRole.CANDIDATE)) {
            return jobApplicationRepository.findByCandidateId(userId)
                    .stream()
                    .map(Mapper::toJobApplicationDTO)
                    .toList();
        }

        return jobApplicationRepository.findByJobOfferRecruiterId(userId)
                .stream()
                .map(Mapper::toJobApplicationDTO)
                .toList();
    }

    public JobApplicationDTO getApplicationById(UUID applicationId, UUID userId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getCandidate().getId().equals(userId) && !application.getJobOffer().getRecruiter().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to view this application");
        }

        return Mapper.toJobApplicationDTO(application);
    }

    public JobApplicationDTO createApplication(UUID jobOfferId,
                                               UUID candidateId,
                                               CreateJobApplicationRequest request) {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (!candidate.getRole().equals(UserRole.CANDIDATE)) {
            throw new RuntimeException("Only candidates can apply for jobs");
        }

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        if (jobApplicationRepository.existsByCandidateIdAndJobOfferId(candidateId, jobOfferId)) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobApplication application = JobApplication.builder()
                .motivationLetter(request.getMotivationLetter())
                .status(ApplicationStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .candidate(candidate)
                .jobOffer(jobOffer)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(application);

        return Mapper.toJobApplicationDTO(savedApplication);
    }

    public JobApplicationDTO updateApplication(UUID applicationId,
                                               UUID candidateId,
                                               CreateJobApplicationRequest request) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getCandidate().getId().equals(candidateId)) {
            throw new RuntimeException("You are not allowed to edit this application");
        }

        if (!application.getStatus().equals(ApplicationStatus.PENDING)) {
            throw new RuntimeException("Only pending applications can be edited");
        }

        application.setMotivationLetter(request.getMotivationLetter());

        jobApplicationRepository.save(application);

        return Mapper.toJobApplicationDTO(application);
    }

    public void withdrawApplication(UUID applicationId, UUID candidateId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getCandidate().getId().equals(candidateId)) {
            throw new RuntimeException("You are not allowed to withdraw this application");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        jobApplicationRepository.save(application);
    }

    public void acceptApplication(UUID applicationId, UUID recruiterId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJobOffer().getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not allowed to accept this application");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        jobApplicationRepository.save(application);
    }

    public void rejectApplication(UUID applicationId, UUID recruiterId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getJobOffer().getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not allowed to reject this application");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        jobApplicationRepository.save(application);
    }
}
