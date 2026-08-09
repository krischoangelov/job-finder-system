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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
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
                .orElseThrow(() -> {
                    log.warn("Cannot fetch applications because userId={} was not found", userId);
                    return new RuntimeException("User not found");
                });

        if (user.getRole().equals(UserRole.CANDIDATE)) {
            List<JobApplicationDTO> applications =
                    jobApplicationRepository.findByCandidateId(userId)
                            .stream()
                            .map(Mapper::toJobApplicationDTO)
                            .toList();

            log.info("Found {} applications for candidateId={}", applications.size(), userId);

            return applications;
        }

        List<JobApplicationDTO> applications =
                jobApplicationRepository.findByJobOfferRecruiterId(userId)
                        .stream()
                        .map(Mapper::toJobApplicationDTO)
                        .toList();

        log.info("Found {} applications for recruiterId={}", applications.size(), userId);

        return applications;
    }

    public JobApplicationDTO getApplicationById(UUID applicationId, UUID userId) {
        JobApplication application =
                jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() -> {

                            log.warn("Job application id={} was not found", applicationId);

                            return new RuntimeException("Application not found");
                        });

        if (!application.getCandidate().getId().equals(userId) && !application.getJobOffer().getRecruiter().getId().equals(userId)) {

            log.warn("Unauthorized attempt by userId={} to view applicationId={}", userId, applicationId);
            throw new RuntimeException("You are not allowed to view this application");
        }

        log.info("Job application id={} successfully retrieved by userId={}", applicationId, userId);

        return Mapper.toJobApplicationDTO(application);
    }

    public JobApplicationDTO createApplication(UUID jobOfferId,
                                               UUID candidateId,
                                               CreateJobApplicationRequest request) {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> {
                    log.warn("Cannot create application because candidateId={} was not found", candidateId);
                    return new RuntimeException("Candidate not found");
                });

        if (!candidate.getRole().equals(UserRole.CANDIDATE)) {

            log.warn("UserId={} attempted to apply for jobOfferId={} without CANDIDATE role", candidateId, jobOfferId);
            throw new RuntimeException("Only candidates can apply for jobs");
        }

        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> {
                    log.warn("Cannot create application because jobOfferId={} was not found", jobOfferId);
                    return new RuntimeException("Job offer not found");
                });

        if (jobApplicationRepository.existsByCandidateIdAndJobOfferId(candidateId, jobOfferId)) {

            log.warn("CandidateId={} attempted to apply twice for jobOfferId={}", candidateId, jobOfferId);

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

        log.info("Job application created successfully with id={} for candidateId={} and jobOfferId={}",
                savedApplication.getId(), candidateId, jobOfferId);

        return Mapper.toJobApplicationDTO(savedApplication);
    }

    public JobApplicationDTO updateApplication(UUID applicationId,
                                               UUID candidateId,
                                               CreateJobApplicationRequest request) {

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.warn("Cannot update application because applicationId={} was not found", applicationId);
                    return new RuntimeException("Application not found");
                });

        if (!application.getCandidate().getId().equals(candidateId)) {

            log.warn("Unauthorized update attempt by candidateId={} for applicationId={}", candidateId, applicationId);

            throw new RuntimeException("You are not allowed to edit this application");
        }

        if (!application.getStatus().equals(ApplicationStatus.PENDING)) {

            log.warn("CandidateId={} attempted to edit applicationId={} with status={}",
                    candidateId, applicationId, application.getStatus());

            throw new RuntimeException("Only pending applications can be edited");
        }

        application.setMotivationLetter(request.getMotivationLetter());

        jobApplicationRepository.save(application);

        log.info("ApplicationId={} successfully updated by candidateId={}", applicationId, candidateId);

        return Mapper.toJobApplicationDTO(application);
    }

    public void withdrawApplication(UUID applicationId, UUID candidateId) {

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.warn("Cannot withdraw application because applicationId={} was not found", applicationId);
                    return new RuntimeException("Application not found");
                });

        if (!application.getCandidate().getId().equals(candidateId)) {

            log.warn("Unauthorized withdrawal attempt by candidateId={} for applicationId={}", candidateId, applicationId);

            throw new RuntimeException("You are not allowed to withdraw this application");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        jobApplicationRepository.save(application);

        log.info("ApplicationId={} successfully withdrawn by candidateId={}", applicationId, candidateId);
    }

    public void acceptApplication(UUID applicationId, UUID recruiterId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.warn("Cannot accept application because applicationId={} was not found", applicationId);
                    return new RuntimeException("Application not found");
                });

        if (!application.getJobOffer().getRecruiter().getId().equals(recruiterId)) {

            log.warn("Unauthorized accept attempt by recruiterId={} for applicationId={}", recruiterId, applicationId);

            throw new RuntimeException("You are not allowed to accept this application");
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        jobApplicationRepository.save(application);

        log.info("ApplicationId={} successfully ACCEPTED by recruiterId={}", applicationId, recruiterId);
    }

    public void rejectApplication(UUID applicationId, UUID recruiterId) {

        JobApplication application = jobApplicationRepository.findById(applicationId)
                        .orElseThrow(() -> {
                            log.warn("Cannot reject application because applicationId={} was not found", applicationId);
                            return new RuntimeException("Application not found");
                        });

        if (!application.getJobOffer().getRecruiter().getId().equals(recruiterId)) {

            log.warn("Unauthorized reject attempt by recruiterId={} for applicationId={}", recruiterId, applicationId);

            throw new RuntimeException("You are not allowed to reject this application");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        jobApplicationRepository.save(application);

        log.info("ApplicationId={} successfully REJECTED by recruiterId={}", applicationId, recruiterId);
    }
}
