package app.model.dto.user;

import app.model.dto.jobapplication.JobApplicationDTO;
import app.model.dto.joboffer.JobOfferDTO;
import app.model.dto.skill.SkillDTO;
import app.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private UserRole role;
    private LocalDateTime createdOn;
    private List<JobOfferDTO> createdJobs;
    private List<JobApplicationDTO> jobApplications;
    private List<SkillDTO> skills;
}
