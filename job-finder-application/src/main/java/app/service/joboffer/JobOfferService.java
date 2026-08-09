package app.service.joboffer;

import app.exception.joboffer.JobOfferAccessDeniedException;
import app.exception.joboffer.JobOfferNotFound;
import app.exception.user.UserNotFoundException;
import app.model.dto.joboffer.CreateJobOfferRequest;
import app.model.dto.joboffer.JobOfferDTO;
import app.model.entity.joboffer.JobOffer;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.repository.joboffer.JobOfferRepository;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class JobOfferService {

    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;

    @Autowired
    public JobOfferService(JobOfferRepository jobOfferRepository, UserRepository userRepository) {
        this.jobOfferRepository = jobOfferRepository;
        this.userRepository = userRepository;
    }

    public JobOfferDTO createJobOffer(UUID recruiterId, CreateJobOfferRequest createJobOfferRequest) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> {
                    log.warn("Cannot create job offer because recruiterId={} was not found", recruiterId);
                    return new UserNotFoundException(recruiterId);
                });

        if (recruiter.getRole().equals(UserRole.CANDIDATE)) {
            log.warn("UserId={} attempted to create a job offer without RECRUITER role", recruiterId);
            throw new JobOfferAccessDeniedException(recruiterId.toString());
        }

        LocalDateTime start = LocalDateTime.now();

        JobOffer jobOffer = JobOffer.builder()
                .title(createJobOfferRequest.getTitle())
                .company(createJobOfferRequest.getCompany())
                .location(createJobOfferRequest.getLocation())
                .description(createJobOfferRequest.getDescription())
                .salary(createJobOfferRequest.getSalary())
                .type(createJobOfferRequest.getType())
                .createdOn(start)
                .deadline(createJobOfferRequest.getDeadline())
                .recruiter(recruiter)
                .build();

        jobOfferRepository.save(jobOffer);

        log.info("Job offer created successfully with id={} by recruiterId={}", jobOffer.getId(), recruiterId);

        return Mapper.toJobOfferDTO(jobOffer);
    }

    public List<JobOfferDTO> getAllJobOffers() {
        List<JobOfferDTO> jobOffers = jobOfferRepository.findAll()
                .stream()
                .map(Mapper::toJobOfferDTO)
                .toList();

        log.info("Retrieved {} job offers", jobOffers.size());

        return jobOffers;
    }

    public List<JobOfferDTO> getAllPostedJobOffersByRecruiter(UUID recruiterId) {

        List<JobOfferDTO> jobOffers = jobOfferRepository.findAllByRecruiterId(recruiterId)
                .stream()
                .map(Mapper::toJobOfferDTO)
                .toList();

        log.info("Retrieved {} job offers posted by recruiterId={}", jobOffers.size(), recruiterId);

        return jobOffers;
    }

    public JobOfferDTO getJobOfferById(UUID id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Job offer id={} was not found", id);
                    return new JobOfferNotFound(id);
                });

        log.info("Job offer id={} retrieved successfully", id);

        return Mapper.toJobOfferDTO(jobOffer);
    }

    public JobOfferDTO updateJobOffer(UUID id, UUID recruiterId, JobOfferDTO jobOfferDTO) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot update job offer because id={} was not found", id);
                    return new JobOfferNotFound(id);
                });

        if (!jobOffer.getRecruiter().getId().equals(recruiterId)) {
            log.warn("Unauthorized update attempt by recruiterId={} for jobOfferId={}", recruiterId, id);
            throw new JobOfferAccessDeniedException(recruiterId.toString());
        }

        jobOffer.setTitle(jobOfferDTO.getTitle());
        jobOffer.setCompany(jobOfferDTO.getCompany());
        jobOffer.setLocation(jobOfferDTO.getLocation());
        jobOffer.setDescription(jobOfferDTO.getDescription());
        jobOffer.setSalary(jobOfferDTO.getSalary());
        jobOffer.setType(jobOfferDTO.getType());
        jobOffer.setDeadline(jobOfferDTO.getDeadline());

        jobOfferRepository.save(jobOffer);

        log.info("Job offer id={} successfully updated by recruiterId={}", id, recruiterId);

        return Mapper.toJobOfferDTO(jobOffer);
    }


    public void deleteJobOffer(UUID jobOfferId, UUID recruiterId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> {
                    log.warn("Cannot delete job offer because id={} was not found", jobOfferId);
                    return new JobOfferNotFound(jobOfferId);
                });

        if (!jobOffer.getRecruiter().getId().equals(recruiterId)) {
            log.warn("Unauthorized delete attempt by recruiterId={} for jobOfferId={}", recruiterId, jobOfferId);
            throw new JobOfferAccessDeniedException(recruiterId.toString());
        }

        jobOfferRepository.delete(jobOffer);

        log.info("Job offer id={} successfully deleted by recruiterId={}", jobOfferId, recruiterId);

    }
}
