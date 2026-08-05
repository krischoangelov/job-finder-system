package app.model.dto.jobapplication;

import app.model.entity.joboffer.JobOffer;
import app.model.entity.user.User;
import app.model.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class JobApplicationDTO {
    private UUID id;
    private String motivationLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private User candidate;
    private JobOffer jobOffer;
}
