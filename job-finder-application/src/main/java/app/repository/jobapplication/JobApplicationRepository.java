package app.repository.jobapplication;

import app.model.entity.jobapplication.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    List<JobApplication> findByCandidateId(UUID candidateId);

    List<JobApplication> findByJobOfferRecruiterId(UUID recruiterId);

    boolean existsByCandidateIdAndJobOfferId(UUID candidateId, UUID jobOfferIdr);
}
