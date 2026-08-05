package app.service.joboffer;

import app.model.dto.joboffer.CreateJobOfferRequest;
import app.model.dto.joboffer.JobOfferDTO;
import app.model.entity.joboffer.JobOffer;
import app.model.entity.user.User;
import app.model.enums.UserRole;
import app.repository.joboffer.JobOfferRepository;
import app.repository.user.UserRepository;
import app.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        User recruiter = userRepository.findById(recruiterId).orElseThrow(
                () -> new RuntimeException("No such recruiter was found")
        );

        if (recruiter.getRole().equals(UserRole.CANDIDATE)) {
            throw new RuntimeException("Candidates cannot create job offers");
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

        return Mapper.toJobOfferDTO(jobOffer);
    }

    public List<JobOfferDTO> getAllJobOffers() {
        return jobOfferRepository.findAll()
                .stream()
                .map(Mapper::toJobOfferDTO)
                .toList();
    }

    public JobOfferDTO getJobOfferById(UUID id) {
        JobOffer jobOffer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such job offer was found"));

        return Mapper.toJobOfferDTO(jobOffer);
    }

    public JobOfferDTO updateJobOffer(UUID id, UUID recruiterId, JobOfferDTO jobOfferDTO) {
        JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No such job offer was found"));

        if (!jobOffer.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not allowed to edit this job offer");
        }

        jobOffer.setTitle(jobOfferDTO.getTitle());
        jobOffer.setCompany(jobOfferDTO.getCompany());
        jobOffer.setLocation(jobOfferDTO.getLocation());
        jobOffer.setDescription(jobOfferDTO.getDescription());
        jobOffer.setSalary(jobOfferDTO.getSalary());
        jobOffer.setType(jobOfferDTO.getType());
        jobOffer.setDeadline(jobOfferDTO.getDeadline());

        jobOfferRepository.save(jobOffer);

        return Mapper.toJobOfferDTO(jobOffer);
    }


    public void deleteJobOffer(UUID jobOfferId, UUID recruiterId) {
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new RuntimeException("Job offer not found"));

        if (!jobOffer.getRecruiter().getId().equals(recruiterId)) {
            throw new RuntimeException("You are not allowed to delete this job offer");
        }

        jobOfferRepository.delete(jobOffer);

    }
}
